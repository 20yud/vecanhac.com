package com.vecanhac.ddd.application.validator;

import com.vecanhac.ddd.domain.discountcode.DiscountCodeEntity;
import com.vecanhac.ddd.domain.discountcodeusage.DiscountCodeUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DiscountValidator {
    private final DiscountCodeUsageRepository usageRepo;

    public void validate(DiscountCodeEntity code, Long userId, int totalQuantity) {
        if (code.getExpiresAt() != null && code.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã giảm giá đã hết hạn");
        }

        if (!code.isActive()) {
            throw new RuntimeException("Mã giảm giá không còn hiệu lực");
        }

        if (totalQuantity < code.getMinQuantity()) {
            throw new RuntimeException("Bạn cần mua ít nhất " + code.getMinQuantity() + " vé để áp dụng mã");
        }

        if (code.getUsageLimit() != null) {
            long used = usageRepo.countByDiscountCode_IdAndIsSuccessfulTrue(code.getId());
            if (used >= code.getUsageLimit()) {
                throw new RuntimeException("Mã đã được sử dụng hết lượt");
            }
        }

        if (code.getUsageLimitPerUser() != null) {
            long usedByUser = usageRepo.countByUser_IdAndDiscountCode_IdAndIsSuccessfulTrue(userId, code.getId());
            if (usedByUser >= code.getUsageLimitPerUser()) {
                throw new RuntimeException("Bạn đã dùng mã này đủ số lần cho phép");
            }
        }
    }
}
