package com.vecanhac.ddd.application.service.email;

public interface EmailService {

    void sendOtp(String toEmail, String otp);
}
