package com.vecanhac.ddd.application.dto.otp;


import com.vecanhac.ddd.domain.model.enums.OtpType;
import lombok.Data;

@Data
public class VerifyOtpRequestDTO {
    private String email;
    private String code;
    private OtpType type; // REGISTER hoặc RESET
}
