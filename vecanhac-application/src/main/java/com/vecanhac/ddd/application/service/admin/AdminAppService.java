package com.vecanhac.ddd.application.service.admin;

import com.vecanhac.ddd.application.dto.admin.AdminEventSummaryDTO;
import com.vecanhac.ddd.application.dto.admin.AdminUserDTO;

import java.util.List;

public interface AdminAppService {

    List<AdminEventSummaryDTO> getEventsByStatus(String statusString);
    void approveEvent(Long eventId);
    void rejectEvent(Long eventId);
    List<AdminUserDTO> getAllUsers();
    List<AdminUserDTO> searchUsersByRole(String roleStr);
    void updateUserRole(Long userId, String newRoleStr);
}
