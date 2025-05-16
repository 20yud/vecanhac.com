package com.vecanhac.ddd.application.dto.event;

import com.vecanhac.ddd.domain.model.enums.EventStatus;
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
    private EventStatus status;
}
