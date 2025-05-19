package com.vecanhac.ddd.controller.admin;

import com.vecanhac.ddd.application.dto.stats.RevenueStatDTO;
import com.vecanhac.ddd.application.dto.stats.SystemStatsDTO;
import com.vecanhac.ddd.application.service.admin.stats.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    @GetMapping
    public ResponseEntity<SystemStatsDTO> getSystemStats() {
        return ResponseEntity.ok(adminStatsService.getSystemStats());
    }

    @GetMapping("/revenue")
    public ResponseEntity<List<RevenueStatDTO>> getRevenueStats(
            @RequestParam(name = "range", defaultValue = "month") String range) {
        return ResponseEntity.ok(adminStatsService.getRevenueStats(range));
    }
}
