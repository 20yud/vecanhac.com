package com.vecanhac.ddd.application.dto.showing;

import com.vecanhac.ddd.application.dto.ticket.TicketDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShowingDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long seatMapId;
    private Boolean isEnabledQueue;

    private List<TicketDTO> tickets;
}