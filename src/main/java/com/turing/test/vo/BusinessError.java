package com.turing.test.vo;

import lombok.AllArgsConstructor;

/**
* @Author Yibo Wen
* @Date 9/2/2021 7:54 PM
**/
@AllArgsConstructor
public enum BusinessError {

    INVALID_PARAM("001","invalid param"),
    ACCESS_NOT_GRANTED("002","access not granted, please contact administrator"),
    UNKNOWN_USER("003","didn't find the user"),
    DUPLICATE_USER("004","the user email already exists"),
    FALLBACK_INTENT("005","chatbot failed to comprehend"),
    INTERNAL_ERROR("006", "chatbot API failure"),
    UNKNOWN_ERROR("999","please take a screenshot of the error and send it to yibowen@usc.edu");

    private final String code;
    private final String msg;

    public String getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }
}
