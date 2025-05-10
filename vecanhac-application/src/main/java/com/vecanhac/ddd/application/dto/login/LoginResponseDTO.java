package com.vecanhac.ddd.application.dto.login;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String fullName;
    private String email;  // 👈 thêm dòng này
    private String role;
}