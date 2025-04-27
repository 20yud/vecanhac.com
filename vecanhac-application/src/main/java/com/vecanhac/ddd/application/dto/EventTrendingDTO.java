package com.vecanhac.ddd.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class EventTrendingDTO {
    private Long id;
    private String title;
    private String slug;
    private String coverImageUrl;
    private BigDecimal minTicketPrice;
    private Long totalSold;
}