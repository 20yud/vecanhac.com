package com.vecanhac.ddd.application.service.auth;

import com.vecanhac.ddd.application.dto.login.LoginRequestDTO;
import com.vecanhac.ddd.application.dto.login.LoginResponseDTO;
import com.vecanhac.ddd.application.dto.register.RegisterRequestDTO;
import com.vecanhac.ddd.application.dto.register.RegisterResponseDTO;
import com.vecanhac.ddd.domain.model.enums.OtpType;

public interface AuthAppService {

    LoginResponseDTO login(LoginRequestDTO request);

    RegisterResponseDTO register(RegisterRequestDTO request);

    String verifyOtp(String email, String code, OtpType type);

    void forgotPassword(String email);
    void resetPassword(String email, String newPassword);
}
