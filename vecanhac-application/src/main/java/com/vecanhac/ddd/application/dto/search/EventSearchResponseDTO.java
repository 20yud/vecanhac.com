package com.vecanhac.ddd.application.dto.search;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class EventSearchResponseDTO {

    private Long id;
    private String title;
    private String slug;
    private String coverImageUrl;
    private LocalDateTime startTime;
    private String venue;
    private Double minTicketPrice;
}
