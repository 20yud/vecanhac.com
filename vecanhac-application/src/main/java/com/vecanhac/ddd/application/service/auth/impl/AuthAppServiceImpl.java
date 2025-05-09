package com.vecanhac.ddd.application.service.auth.impl;

import com.vecanhac.ddd.application.dto.login.LoginRequestDTO;
import com.vecanhac.ddd.application.dto.login.LoginResponseDTO;
import com.vecanhac.ddd.application.dto.register.RegisterRequestDTO;
import com.vecanhac.ddd.application.dto.register.RegisterResponseDTO;
import com.vecanhac.ddd.application.exception.EmailAlreadyExistException;
import com.vecanhac.ddd.application.exception.InvalidPasswordException;
import com.vecanhac.ddd.application.exception.OtpInvalidException;
import com.vecanhac.ddd.application.exception.UserNotFoundException;
import com.vecanhac.ddd.application.service.auth.AuthAppService;
import com.vecanhac.ddd.application.service.email.EmailService;
import com.vecanhac.ddd.domain.token.TokenProvider;
import com.vecanhac.ddd.domain.model.enums.OtpType;
import com.vecanhac.ddd.domain.otp.OtpCodeEntity;
import com.vecanhac.ddd.domain.otp.OtpCodeRepository;
import com.vecanhac.ddd.domain.user.UserEntity;
import com.vecanhac.ddd.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthAppServiceImpl implements AuthAppService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpCodeRepository otpCodeRepository;
    private final EmailService emailService;
    private final TokenProvider tokenProvider;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Email không tồn tại"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException("Mật khẩu không đúng");
        }

        if (!user.isVerified()) {
            throw new OtpInvalidException("Tài khoản chưa xác thực OTP");
        }

        String token = tokenProvider.generateToken(user.getEmail());

        return new LoginResponseDTO(token, user.getFullName(), user.getEmail(), user.getRole());
    }

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistException("Email đã được đăng ký");
        }

        String randomPhone = "000" + ((int)(Math.random() * 9000000) + 1000000);
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setFullName("Người dùng mới");
        user.setPhoneNumber(randomPhone);
        user.setCreatedAt(LocalDateTime.now());
        user.setVerified(false); // ⚠️ chưa xác thực
        userRepository.save(user);

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        OtpCodeEntity otpEntity = new OtpCodeEntity();
        otpEntity.setEmailOrPhone(request.getEmail());
        otpEntity.setCode(otp);
        otpEntity.setType(OtpType.register);
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpEntity.setUsed(false);
        otpCodeRepository.save(otpEntity);

        emailService.sendOtp(request.getEmail(), otp);
        return new RegisterResponseDTO("Đăng ký thành công. Vui lòng kiểm tra email để xác thực mã OTP.");
    }

    @Override
    public String verifyOtp(String email, String code, OtpType type) {
        OtpCodeEntity otp = otpCodeRepository
                .findByEmailOrPhoneAndCodeAndType(email, code, type)
                .orElseThrow(() -> new OtpInvalidException("Mã OTP không hợp lệ"));

        if (otp.isUsed() || otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new OtpInvalidException("Mã OTP đã hết hạn hoặc đã sử dụng");
        }

        otp.setUsed(true);
        otpCodeRepository.save(otp);

        // ✅ Nếu là đăng ký thì cập nhật user thành verified
        if (type == OtpType.register) {
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng"));
            user.setVerified(true);
            userRepository.save(user);
        }

        return "Xác minh OTP thành công";
    }

    @Override
    public void forgotPassword(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email không tồn tại"));

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        OtpCodeEntity otpEntity = new OtpCodeEntity();
        otpEntity.setEmailOrPhone(email);
        otpEntity.setCode(otp);
        otpEntity.setType(OtpType.reset);
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpEntity.setUsed(false);
        otpCodeRepository.save(otpEntity);

        emailService.sendOtp(email, otp);
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email không tồn tại"));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}