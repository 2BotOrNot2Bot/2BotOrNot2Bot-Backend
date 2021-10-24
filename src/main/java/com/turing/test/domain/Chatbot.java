package com.turing.test.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @Author Yibo Wen
* @Date 2021/10/24 15:19
**/
@Data
@NoArgsConstructor
public class Chatbot {

    private String name;

    private Integer testCount;

    private Integer successCount;

    private Double percentage;

}
