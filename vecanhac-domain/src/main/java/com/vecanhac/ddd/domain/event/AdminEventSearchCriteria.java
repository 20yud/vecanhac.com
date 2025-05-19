package com.vecanhac.ddd.domain.event;

import lombok.Data;

@Data
public class AdminEventSearchCriteria {
    private String status = "ALL";
    private String keyword;
    private int page = 0;
    private int size = 10;
}
