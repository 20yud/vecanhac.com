package com.vecanhac.ddd.application.dto.login;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
