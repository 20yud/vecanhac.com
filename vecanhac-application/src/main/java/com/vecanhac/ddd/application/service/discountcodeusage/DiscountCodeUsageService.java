package com.vecanhac.ddd.application.service.discountcodeusage;

import com.vecanhac.ddd.domain.discountcode.DiscountCodeEntity;
import com.vecanhac.ddd.domain.user.UserEntity;

public interface DiscountCodeUsageService {

    boolean hasUserUsedCode(Long userId, Long discountCodeId);
    void recordUsage(DiscountCodeEntity code, UserEntity user, Long orderId, boolean successful);

}
