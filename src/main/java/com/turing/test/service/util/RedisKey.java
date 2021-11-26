package com.turing.test.service.util;

import lombok.AllArgsConstructor;

/**
* @Author Yibo Wen
* @Date 11/18/2021 10:45 PM
**/
@AllArgsConstructor
public enum RedisKey {

    WAITING_QUEUE("waiting"),
    CHATTING_STATUS("chatting");

    private String key;

    public String getKey(){return key;}

}
