package com.vecanhac.ddd.controller.auth;


import com.vecanhac.ddd.application.dto.login.LoginRequestDTO;
import com.vecanhac.ddd.application.dto.login.LoginResponseDTO;
import com.vecanhac.ddd.application.dto.otp.ForgotPasswordRequestDTO;
import com.vecanhac.ddd.application.dto.otp.ResetPasswordRequestDTO;
import com.vecanhac.ddd.application.dto.otp.VerifyOtpRequestDTO;
import com.vecanhac.ddd.application.dto.register.RegisterRequestDTO;
import com.vecanhac.ddd.application.dto.register.RegisterResponseDTO;
import com.vecanhac.ddd.application.service.auth.AuthAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthAppService authAppService;


    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {


        return authAppService.login(request);
    }



    @PostMapping("/register")
    public RegisterResponseDTO register(@RequestBody RegisterRequestDTO request) {
        return authAppService.register(request);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody VerifyOtpRequestDTO request) {
        String message = authAppService.verifyOtp(request.getEmail(), request.getCode(), request.getType());
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        authAppService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("OTP đã được gửi đến email.");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        authAppService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok("Đặt lại mật khẩu thành công.");
    }


}
