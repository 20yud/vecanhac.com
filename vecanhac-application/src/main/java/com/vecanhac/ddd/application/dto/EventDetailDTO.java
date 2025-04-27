package com.vecanhac.ddd.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDetailDTO {
    private Long id;
    private String title;
    private String description;
    private String slug;
    private String coverImageUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String venue;
    private Double minTicketPrice;
    private String andress;
}