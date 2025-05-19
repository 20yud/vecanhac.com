package com.vecanhac.ddd.application.dto.admin;
import com.vecanhac.ddd.domain.model.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminEventSummaryDTO {
    private Long id;
    private String title;
    private String venue;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private EventStatus status;
    private String organizerName;
}
