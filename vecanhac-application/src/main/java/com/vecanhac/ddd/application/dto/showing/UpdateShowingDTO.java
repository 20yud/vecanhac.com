package com.vecanhac.ddd.application.dto.showing;

import com.vecanhac.ddd.application.dto.ticket.UpdateTicketDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateShowingDTO {
    @NotNull
    private Long id; // bắt buộc
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long seatMapId;
    private Boolean isEnabledQueue;

    @Valid
    private List<UpdateTicketDTO> tickets; // hoặc Optional nếu cho phép null
}