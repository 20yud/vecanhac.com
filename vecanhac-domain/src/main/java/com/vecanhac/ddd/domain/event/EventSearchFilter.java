package com.vecanhac.ddd.domain.event;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventSearchFilter {
    private String keyword;
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean freeOnly;
    private Long categoryId;
}