package com.vecanhac.ddd.application.dto.event.myevents;

import com.vecanhac.ddd.application.dto.showing.UpdateShowingDTO;
import jakarta.validation.Valid;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PatchUpdateEventDTO {
    private String title;
    private String description;
    private String coverImageUrl;
    private String organizerName;
    private String organizerLogoUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String venue;
    private String address;
    private Long locationId;

    @Valid
    private List<UpdateShowingDTO> showings;

    private String status; // 👈 thêm trường này

}
