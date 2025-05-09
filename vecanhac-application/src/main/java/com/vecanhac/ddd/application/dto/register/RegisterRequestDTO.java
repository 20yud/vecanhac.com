package com.vecanhac.ddd.application.dto.register;


import lombok.Data;

@Data
public class RegisterRequestDTO {

    private String email;

    private String password;
}
