package com.vecanhac.ddd.application.service.profile.impl;

import com.vecanhac.ddd.application.dto.profile.UpdateUserProfileDTO;
import com.vecanhac.ddd.application.dto.profile.UserProfileDTO;
import com.vecanhac.ddd.application.service.profile.UserProfileAppService;
import com.vecanhac.ddd.domain.user.UserEntity;
import com.vecanhac.ddd.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileAppServiceImpl implements UserProfileAppService {

    private final UserRepository userRepository;

    @Override
    public UserProfileDTO getProfile(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserProfileDTO(
                user.getFullName(),
                user.getPhoneNumber(),
                user.getEmail()
        );
    }

    @Override
    public void updateProfile(String email, UpdateUserProfileDTO dto) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(dto.getFullName());
        user.setPhoneNumber(dto.getPhoneNumber());


        userRepository.save(user);
    }
}
