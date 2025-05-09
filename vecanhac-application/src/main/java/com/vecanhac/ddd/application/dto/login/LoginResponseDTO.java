package com.vecanhac.ddd.application.dto.login;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {


    private String token;
    private String fullName;
    private String email;
    private String role;
}
