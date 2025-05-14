package com.vecanhac.ddd.application.service.discount;

import com.vecanhac.ddd.application.dto.discount.DiscountRequestDTO;
import com.vecanhac.ddd.application.dto.discount.DiscountResponseDTO;

public interface DiscountCodeAppService {
    DiscountResponseDTO checkDiscount(DiscountRequestDTO request);
}