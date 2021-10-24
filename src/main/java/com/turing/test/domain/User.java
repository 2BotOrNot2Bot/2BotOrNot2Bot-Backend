package com.turing.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
* @Author Yibo Wen
* @Date 2021/10/24 0:17
**/
@Data
@AllArgsConstructor
public class User {

    private Long id;

    private String email;

    private String password;

    private Integer role;

}
