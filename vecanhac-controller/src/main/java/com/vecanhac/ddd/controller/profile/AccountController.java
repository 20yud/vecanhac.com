package com.vecanhac.ddd.controller.profile;

import com.vecanhac.ddd.application.dto.profile.UpdateUserProfileDTO;
import com.vecanhac.ddd.application.dto.profile.UserProfileDTO;
import com.vecanhac.ddd.application.service.profile.UserProfileAppService;
import com.vecanhac.ddd.domain.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserProfileAppService userProfileAppService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getEmail(); // dùng getEmail() chứ không phải getUsername()
        UserProfileDTO profile = userProfileAppService.getProfile(email);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                              @RequestBody UpdateUserProfileDTO dto) {
        String email = principal.getEmail();
        userProfileAppService.updateProfile(email, dto);
        return ResponseEntity.ok().build();
    }
}