package com.vecanhac.ddd.application.dto.showing;

import com.vecanhac.ddd.application.dto.ticket.CreateTicketDTO;
import jakarta.validation.Valid;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateShowingDTO {
    @NotNull
    private LocalDateTime startTime;
    @NotNull private LocalDateTime endTime;
    private Long seatMapId;
    private Boolean isEnabledQueue;

    @Valid
    private List<CreateTicketDTO> tickets;
}