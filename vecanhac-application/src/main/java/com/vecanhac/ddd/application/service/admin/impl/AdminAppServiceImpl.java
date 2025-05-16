package com.vecanhac.ddd.application.service.admin.impl;

import com.vecanhac.ddd.application.dto.admin.AdminEventSummaryDTO;
import com.vecanhac.ddd.application.exception.BadRequestException;
import com.vecanhac.ddd.application.service.admin.AdminAppService;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.event.EventRepository;
import com.vecanhac.ddd.domain.model.enums.EventStatus;
import com.vecanhac.ddd.domain.model.enums.UserRole;
import com.vecanhac.ddd.domain.user.UserEntity;
import com.vecanhac.ddd.domain.user.UserRepository;
import com.vecanhac.ddd.application.dto.admin.AdminUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AdminAppServiceImpl implements AdminAppService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public List<AdminEventSummaryDTO> getEventsByStatus(String statusString) {
        EventStatus status = EventStatus.valueOf(statusString.toUpperCase());
        return eventRepository.findByStatus(status)
                .stream()
                .map(event -> new AdminEventSummaryDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getVenue(),
                        event.getStartTime(),
                        event.getStatus().name()
                ))
                .toList();
    }

    @Transactional
    public void approveEvent(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));
        event.setStatus(EventStatus.PUBLISHED); // ✅ Published sau khi admin duyệt
    }

    @Transactional
    public void rejectEvent(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));
        event.setStatus(EventStatus.REJECT); // ✅ Trả về nháp nếu bị từ chối
    }



    @Override
    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new AdminUserDTO(
                        u.getId(),
                        u.getEmail(),
                        u.getFullName(),
                        u.getRole().name(),
                        u.isVerified()
                ))
                .toList();
    }

    @Override
    public List<AdminUserDTO> searchUsersByRole(String roleStr) {
        try {
            UserRole role = UserRole.valueOf(roleStr.toUpperCase());
            return userRepository.findByRole(role).stream()
                    .map(u -> new AdminUserDTO(
                            u.getId(),
                            u.getEmail(),
                            u.getFullName(),
                            u.getRole().name(),
                            u.isVerified()
                    ))
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Vai trò không hợp lệ: " + roleStr);
        }
    }



    @Transactional
    public void updateUserRole(Long userId, String newRoleStr) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        UserRole newRole = UserRole.valueOf(newRoleStr.toUpperCase());

        if (user.getRole() != newRole) {
            user.setRole(newRole);
        }
    }
}
