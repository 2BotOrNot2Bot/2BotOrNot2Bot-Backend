package com.turing.test.domain.enums;

import lombok.AllArgsConstructor;

/**
* @Author Yibo Wen
* @Date 10/28/2021 3:49 PM
**/
@AllArgsConstructor
public enum Chatbots {

    DIALOGFLOW("dialogflow"),
    BRAINSHOP("brainshop"),
    AICHATBOT("aichatbot"),
    ROBOMATIC("robomatic");

    private final String name;

    public String getName(){
        return this.name;
    }

    public static String getRandomName() {
        return values()[(int)(Math.random() * Chatbots.values().length)].getName();
    }

}
