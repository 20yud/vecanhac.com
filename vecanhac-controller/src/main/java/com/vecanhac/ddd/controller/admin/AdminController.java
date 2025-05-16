package com.vecanhac.ddd.controller.admin;


import com.vecanhac.ddd.application.dto.admin.AdminEventSummaryDTO;
import com.vecanhac.ddd.application.dto.admin.AdminUserDTO;
import com.vecanhac.ddd.application.dto.admin.UpdateUserRoleDTO;
import com.vecanhac.ddd.application.service.admin.AdminAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminAppService adminAppService;

    @GetMapping("/only")
    public ResponseEntity<?> testOrganizerAccess() {
        return ResponseEntity.ok("Bạn là ADMIN");
    }



    @GetMapping
    public ResponseEntity<List<AdminEventSummaryDTO>> getPendingEvents(
            @RequestParam(name = "status", defaultValue = "PENDING") String status) {
        return ResponseEntity.ok(adminAppService.getEventsByStatus(status));
    }

    @PatchMapping("/{eventId}/approve")
    public ResponseEntity<String> approveEvent(
            @PathVariable(name = "eventId") Long eventId) {
        adminAppService.approveEvent(eventId);
        return ResponseEntity.ok("Sự kiện đã được duyệt");
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<String> rejectEvent(
            @PathVariable(name = "eventId") Long eventId) {
        adminAppService.rejectEvent(eventId);
        return ResponseEntity.ok("Sự kiện đã bị từ chối");
    }


    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        System.out.println("📢 GỌI getAllUsers()");
        return ResponseEntity.ok(adminAppService.getAllUsers());
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<AdminUserDTO>> searchUsersByRole(
            @RequestParam(name = "role") String role) {
        System.out.println("📢 GỌI searchUsersByRole(" + role + ")");
        return ResponseEntity.ok(adminAppService.searchUsersByRole(role));
    }



    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable(name = "userId") Long userId,
            @RequestBody UpdateUserRoleDTO request) {
        adminAppService.updateUserRole(userId, request.getRole());
        return ResponseEntity.ok("Cập nhật vai trò thành công");
    }
}
