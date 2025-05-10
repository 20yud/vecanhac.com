package com.vecanhac.ddd.application.service.profile;


import com.vecanhac.ddd.application.dto.profile.UpdateUserProfileDTO;
import com.vecanhac.ddd.application.dto.profile.UserProfileDTO;

public interface UserProfileAppService {
    UserProfileDTO getProfile(String email);
    void updateProfile(String email, UpdateUserProfileDTO dto);
}