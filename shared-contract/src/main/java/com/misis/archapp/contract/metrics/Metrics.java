package com.misis.archapp.contract.metrics;

public abstract class Metrics {

    // Counter метрика - сколько всего было создано пользователей
    public static final String USERS_CREATED_TOTAL = "users.new";

    // Summary метрика - сколько времени обрабатывался запрос на создание пользователя
    public static final String API_USER_REQ_DURATION = "api.user.request.duration";
    public static final String METHOD_TAG = "method";
    public static final String POST_TAG_VAL = "post";
    public static final String GET_TAG_VAL = "get";

}