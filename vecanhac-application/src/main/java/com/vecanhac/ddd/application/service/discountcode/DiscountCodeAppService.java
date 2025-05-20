package com.vecanhac.ddd.application.service.discountcode;

import com.vecanhac.ddd.application.dto.discount.DiscountRequestDTO;
import com.vecanhac.ddd.application.dto.discount.DiscountResponseDTO;

public interface DiscountCodeAppService {
    DiscountResponseDTO checkDiscount(DiscountRequestDTO request, Long userId);
}