package com.vecanhac.ddd.application.dto.disocuntusage;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountUsageDTO {
    private Long id;
    private String code;
    private int percentage;
    private BigDecimal fixedAmount;
    private Integer usageLimit;
    private Integer usageLimitPerUser;
    private int totalUsed;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}