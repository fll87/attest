package com.qzt.module.system.domain.vo.user;

import lombok.Data;

@Data
public class LoginVO {

    private String username;

    private String password;

    private String code;

    private String uuid;

    private String type;

}
