package com.vecanhac.ddd.application.dto.stats;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class SystemStatsDTO {
    private long totalUsers;
    private long totalEvents;
    private long totalShowings;
    private long totalTickets;
    private long totalOrders;
    private long activeDiscountCodes;
    private BigDecimal totalRevenue;

    private Map<String, Long> eventStatusMap;
    private Map<String, Long> orderStatusMap;
    private Map<String, Long> ticketStatusMap;
}