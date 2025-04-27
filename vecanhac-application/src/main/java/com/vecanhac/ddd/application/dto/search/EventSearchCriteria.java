package com.vecanhac.ddd.application.dto.search;


import lombok.Data;

import java.time.LocalDate;

@Data
public class EventSearchCriteria {

    private String keyword;
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean freeOnly;
    private Long categoryId;
}
