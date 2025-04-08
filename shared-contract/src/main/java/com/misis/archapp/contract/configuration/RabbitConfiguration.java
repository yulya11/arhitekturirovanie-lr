package com.misis.archapp.contract.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

   public static final String USER_QUEUE = "user.events";
   public static final String USER_EXCHANGE = "user.exchange";
   public static final String USER_ROUTING_KEY = "user.#";

   @Bean
   public Jackson2JsonMessageConverter converter() {
      return new Jackson2JsonMessageConverter();
   }

   @Bean
   public Queue userQueue() {
      return new Queue(USER_QUEUE, true);
   }

   @Bean
   public TopicExchange userExchange() {
      return new TopicExchange(USER_EXCHANGE);
   }

   @Bean
   public Binding userBinding(Queue userQueue, TopicExchange userExchange) {
      return BindingBuilder.bind(userQueue).to(userExchange).with(USER_ROUTING_KEY);
   }
}