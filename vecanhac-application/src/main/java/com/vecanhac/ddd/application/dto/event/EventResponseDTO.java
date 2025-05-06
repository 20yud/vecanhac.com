package com.vecanhac.ddd.application.dto.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventResponseDTO {


    private Long id;
    private String title;
    private String slug;
    private String coverImageUrl;
    private LocalDateTime startTime;
    private Double minTicketPrice;
}
