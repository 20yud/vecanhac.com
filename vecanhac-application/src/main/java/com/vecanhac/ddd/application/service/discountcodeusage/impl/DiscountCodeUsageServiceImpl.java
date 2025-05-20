package com.vecanhac.ddd.application.service.discountcodeusage.impl;

import com.vecanhac.ddd.application.service.discountcodeusage.DiscountCodeUsageService;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeEntity;
import com.vecanhac.ddd.domain.discountcodeusage.DiscountCodeUsageEntity;
import com.vecanhac.ddd.domain.discountcodeusage.DiscountCodeUsageRepository;
import com.vecanhac.ddd.domain.order.OrderEntity;
import com.vecanhac.ddd.domain.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountCodeUsageServiceImpl implements DiscountCodeUsageService {

    private final DiscountCodeUsageRepository usageRepository;

    @Override
    public boolean hasUserUsedCode(Long userId, Long discountCodeId) {
        return usageRepository
                .findByUser_IdAndDiscountCode_Id(userId, discountCodeId)
                .map(DiscountCodeUsageEntity::isSuccessful)
                .orElse(false);
    }

    @Override
    public void recordUsage(DiscountCodeEntity code, UserEntity user, Long orderId, boolean successful) {
        Optional<DiscountCodeUsageEntity> existing = usageRepository
                .findByUser_IdAndDiscountCode_Id(user.getId(), code.getId());

        if (existing.isEmpty()) {
            usageRepository.save(DiscountCodeUsageEntity.builder()
                    .discountCode(code)
                    .user(user)
                    .order(OrderEntity.builder().id(orderId).build())
                    .usedAt(LocalDateTime.now())
                    .isSuccessful(successful)
                    .build());
        } else if (!existing.get().isSuccessful() && successful) {
            DiscountCodeUsageEntity usage = existing.get();
            usage.setSuccessful(true);
            usage.setUsedAt(LocalDateTime.now());
            usage.setOrder(OrderEntity.builder().id(orderId).build());
            usageRepository.save(usage);
        }
    }



}
