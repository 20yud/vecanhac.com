package com.vecanhac.ddd.controller.admin;


import com.vecanhac.ddd.application.dto.admin.AdminEventSummaryDTO;
import com.vecanhac.ddd.application.dto.admin.AdminUserDTO;
import com.vecanhac.ddd.application.dto.admin.UpdateUserRoleDTO;
import com.vecanhac.ddd.application.service.admin.AdminAppService;
import com.vecanhac.ddd.application.service.event.EventAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminAppService adminAppService;
    private final EventAppService eventAppService;

    @GetMapping("/only")
    public ResponseEntity<?> testOrganizerAccess() {
        return ResponseEntity.ok("Bạn là ADMIN");
    }

    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        System.out.println("📢 GỌI getAllUsers()");
        return ResponseEntity.ok(adminAppService.getAllUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<AdminUserDTO>> searchUsersByRole(
            @RequestParam(name = "role") String role) {
        System.out.println("📢 GỌI searchUsersByRole(" + role + ")");
        return ResponseEntity.ok(adminAppService.searchUsersByRole(role));
    }



    @PatchMapping("/{userId}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable(name = "userId") Long userId,
            @RequestBody UpdateUserRoleDTO request) {
        adminAppService.updateUserRole(userId, request.getRole());
        return ResponseEntity.ok("Cập nhật vai trò thành công");
    }


}
