package com.vecanhac.ddd.controller.auth;


import com.vecanhac.ddd.application.dto.login.LoginRequestDTO;
import com.vecanhac.ddd.application.dto.login.LoginResponseDTO;
import com.vecanhac.ddd.application.dto.otp.ForgotPasswordRequestDTO;
import com.vecanhac.ddd.application.dto.otp.ResetPasswordRequestDTO;
import com.vecanhac.ddd.application.dto.otp.VerifyOtpRequestDTO;
import com.vecanhac.ddd.application.dto.profile.UpdateUserProfileDTO;
import com.vecanhac.ddd.application.dto.profile.UserProfileDTO;
import com.vecanhac.ddd.application.dto.register.RegisterRequestDTO;
import com.vecanhac.ddd.application.dto.register.RegisterResponseDTO;
import com.vecanhac.ddd.application.service.auth.AuthAppService;
import com.vecanhac.ddd.application.service.profile.UserProfileAppService;
import com.vecanhac.ddd.domain.security.UserPrincipal;
import com.vecanhac.ddd.domain.token.TokenProvider;
import com.vecanhac.ddd.domain.user.UserEntity;
import com.vecanhac.ddd.domain.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthAppService authAppService;
    private final UserProfileAppService userProfileAppService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authAppService.login(request);

        ResponseCookie cookie = ResponseCookie.from("token", response.getToken())
                .httpOnly(false)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(86400)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
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

    // ✅ Thông tin user hiện tại
    @GetMapping("/me")
    public ResponseEntity<CurrentUserDTO> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(new CurrentUserDTO(
                principal.getEmail(),
                principal.getFullName(),
                principal.getRole()
        ));
    }

    // ✅ Hồ sơ chi tiết user
    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileDTO> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        UserProfileDTO profile = userProfileAppService.getProfile(principal.getEmail());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me/profile")
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                              @RequestBody UpdateUserProfileDTO dto) {
        userProfileAppService.updateProfile(principal.getEmail(), dto);
        return ResponseEntity.ok().build();
    }

    // Response DTO dùng cho /me
    public record CurrentUserDTO(String email, String fullName, String role) {}
}
