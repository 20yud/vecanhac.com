package com.vecanhac.ddd.domain.event.myevents;

import com.vecanhac.ddd.domain.model.enums.EventStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyEventSearchFilter {
    private Long organizerId;
    private String keyword;
    private EventStatus status;     // PENDING, DRAFT, PUBLISHED
    private Boolean upcoming;       // true = sắp tới, false = đã qua
}