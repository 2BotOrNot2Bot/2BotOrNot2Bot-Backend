package com.turing.test.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @Author Yibo Wen
* @Date 2021/10/24 0:17
**/
@Data
@NoArgsConstructor
public class User {

    private Long id;

    private String email;

    private String password;

    private Integer role;

}
