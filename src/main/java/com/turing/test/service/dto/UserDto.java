package com.turing.test.service.dto;

import lombok.Data;

/**
* @Author Yibo Wen
* @Date 2021/9/23 21:41
**/
@Data
public class UserDto {

    private Long uid;

    private String email;

    private String points;

    private Integer role;

}
