package com.vecanhac.ddd.application.dto.admin;

import lombok.Data;

@Data
public class UpdateUserRoleDTO {
    private String role; // USER | ORG
}