package com.misis.archapp.user.service;

import com.misis.archapp.contract.dto.UserCreatedEvent;
import com.misis.archapp.contract.metrics.Metrics;
import com.misis.archapp.user.db.User;
import com.misis.archapp.user.db.UserRepository;
import com.misis.archapp.user.dto.UserCreateDTO;
import com.misis.archapp.user.dto.UserDTO;
import com.misis.archapp.user.dto.UserUpdateDTO;
import com.misis.archapp.user.dto.mapper.UserMapper;
import com.misis.archapp.user.service.cache.UserCacheService;
import com.misis.archapp.user.service.publisher.UserEventPublisher;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserCacheService userCacheService;
    private final UserEventPublisher userEventPublisher;
    private final MeterRegistry meterRegistry;

    @Autowired
    public UserService(
        UserRepository userRepository,
        UserMapper userMapper,
        UserCacheService userCacheService,
        UserEventPublisher userEventPublisher,
        MeterRegistry meterRegistry
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userCacheService = userCacheService;
        this.userEventPublisher = userEventPublisher;
        this.meterRegistry = meterRegistry;
    }

    public List<UserDTO> getAllUsers() {
        return userMapper.toDTOList(userRepository.findAll());
    }

    public UserDTO getUserById(UUID id) {
        Optional<UserDTO> cachedUser = userCacheService.getFromCache(id);

        // cache hit - нашел пользователя в кэше
        //noinspection OptionalIsPresent
        if (cachedUser.isPresent()) {
            LOGGER.info("User cache hit");
            return cachedUser.get();
        }

        LOGGER.info("User cache miss");
        // cache miss - пользователя в кэше не оказалось
        UserDTO userFromDB = userRepository.findById(id).map(userMapper::toDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // актуализирует кэш значением из БД
        userCacheService.saveToCache(userFromDB);
        return userFromDB;
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        User user = userMapper.toEntity(userCreateDTO);
        User savedUser = userRepository.save(user);

        // отправляет ивент с данными о пользователе
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(user.getId(), user.getEmail(), user.getName());
        userEventPublisher.publishUserEvent(userCreatedEvent);

        // после того как пользователь был создан и ивент отправлен - увеличивает значение метрики
        meterRegistry.counter(Metrics.USERS_CREATED_TOTAL).increment();
        return userMapper.toDTO(savedUser);
    }

    public UserDTO updateUser(UUID id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (userUpdateDTO.name().isPresent()) {
            user.setName(userUpdateDTO.name().get());
        }

        if (userUpdateDTO.email().isPresent()) {
            user.setEmail(userUpdateDTO.email().get());
        }

        User savedUser = userRepository.save(user);

        // после обновления - очищает данные из кэша
        LOGGER.info("User cache evict on update");
        userCacheService.removeFromCache(user.getId());

        return userMapper.toDTO(savedUser);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
        // после удаления - очищает данные из кэша
        LOGGER.info("User cache evict on delete");
        userCacheService.removeFromCache(id);
    }

}
