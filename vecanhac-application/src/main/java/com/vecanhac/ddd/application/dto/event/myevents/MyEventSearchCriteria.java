package com.vecanhac.ddd.application.dto.event.myevents;

import lombok.Data;

@Data
public class MyEventSearchCriteria {
    private String keyword;
    private String status; // PENDING, DRAFT, PUBLISHED
    private String time;   // upcoming, past
}