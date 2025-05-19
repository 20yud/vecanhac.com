package com.vecanhac.ddd.application.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RevenueStatDTO {
    private String time; // e.g., "2025-05" or "2025-05-19"
    private BigDecimal revenue;
}