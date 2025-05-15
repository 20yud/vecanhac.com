package com.vecanhac.ddd.application.dto.event;

import com.vecanhac.ddd.application.dto.showing.CreateShowingDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateEventRequestDTO {
    private String title;
    private String slug;
    private String description;
    private String coverImageUrl;

    private Long organizerId;
    private String organizerName;
    private String organizerLogoUrl;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String venue;
    private String address;

    private Long locationId;

    @Valid
    @NotEmpty
    private List<CreateShowingDTO> showings;
}
