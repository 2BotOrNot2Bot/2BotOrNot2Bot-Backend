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

    private Long uid;

    private String firebaseUid;

    private Integer points;

    private Integer role;

}
