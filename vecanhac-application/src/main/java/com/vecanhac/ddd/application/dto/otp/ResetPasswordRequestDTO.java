package com.vecanhac.ddd.application.dto.otp;

import lombok.Data;

@Data
public class ResetPasswordRequestDTO {

    private String email;
    private String newPassword;

}
