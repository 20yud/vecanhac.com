package com.vecanhac.ddd.application.dto.event;

import com.vecanhac.ddd.application.dto.showing.ShowingDTO;
import com.vecanhac.ddd.application.dto.ticket.TicketDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventDetailDTO {
    private Long id;
    private String title;
    private String description;
    private String slug;
    private String orgName;
    private String coverImageUrl;
    private String orgImageUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String venue;
    private Double minTicketPrice;
    private String andress;

    private String status; // 👈 thêm trường này

    private List<ShowingDTO> showings;
}