package com.vecanhac.ddd.application.dto.admin;
import java.time.LocalDateTime;

public record AdminEventSummaryDTO(
        Long id,
        String title,
        String venue,
        LocalDateTime startTime,
        String status
) {
}