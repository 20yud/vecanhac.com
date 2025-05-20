package com.vecanhac.ddd.application.dto.discount;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdateDiscountDTO {
    private String code;
    private Integer percentage; // nullable nếu dùng fixedAmount
    private BigDecimal fixedAmount;
    private Integer minQuantity;
    private BigDecimal maxDiscountAmount;
    private Integer usageLimit;
    private Integer usageLimitPerUser;
    private Long applicableEventId; // null = toàn hệ thống
    private Boolean isActive;
    private LocalDateTime expiresAt;
}