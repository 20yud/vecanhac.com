package com.vecanhac.ddd.application.service.admin;

import com.vecanhac.ddd.application.dto.admin.AdminEventSummaryDTO;
import com.vecanhac.ddd.application.dto.admin.AdminUserDTO;
import com.vecanhac.ddd.application.dto.event.EventDetailDTO;
import com.vecanhac.ddd.application.dto.event.EventResponseDTO;
import com.vecanhac.ddd.application.dto.event.myevents.PatchUpdateEventDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdminAppService {

    List<AdminEventSummaryDTO> getEventsByStatus(String statusString);
    void approveEvent(Long eventId);
    void rejectEvent(Long eventId);
    List<AdminUserDTO> getAllUsers();
    List<AdminUserDTO> searchUsersByRole(String roleStr);
    void updateUserRole(Long userId, String newRoleStr);

    List<EventResponseDTO> getAllEventsForAdmin(String statusStr);
    EventDetailDTO getEventDetailAsAdmin(Long eventId);
    EventResponseDTO patchUpdateEventAsAdmin(Long eventId, PatchUpdateEventDTO request);
    void deleteEventAsAdmin(Long eventId);






}
