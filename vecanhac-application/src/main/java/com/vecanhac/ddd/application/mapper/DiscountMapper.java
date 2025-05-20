package com.vecanhac.ddd.application.mapper;


import com.vecanhac.ddd.application.dto.disocuntusage.DiscountUsageDTO;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeEntity;

public class DiscountMapper {

    public static DiscountUsageDTO toUsageDTO(DiscountCodeEntity entity, long totalUsed) {
        return DiscountUsageDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .percentage(entity.getPercentage())
                .fixedAmount(entity.getFixedAmount())
                .minQuantity(entity.getMinQuantity())
                .applicableEventId(
                        entity.getApplicableEvent() != null ? entity.getApplicableEvent().getId() : null
                )
                .usageLimit(entity.getUsageLimit())
                .maxDiscountAmount(entity.getMaxDiscountAmount())
                .usageLimitPerUser(entity.getUsageLimitPerUser())
                .totalUsed((int) totalUsed)
                .isActive(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .expiresAt(entity.getExpiresAt())
                .build();
    }
}