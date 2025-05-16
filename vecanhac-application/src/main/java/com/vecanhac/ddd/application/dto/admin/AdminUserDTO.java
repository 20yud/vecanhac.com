package com.vecanhac.ddd.application.dto.admin;

public record AdminUserDTO(
        Long id,
        String email,
        String fullName,
        String role,
        Boolean isVerified
) {}