package com.vecanhac.ddd.application.service.admin;

import com.vecanhac.ddd.application.dto.discount.CreateOrUpdateDiscountDTO;
import com.vecanhac.ddd.application.dto.disocuntusage.DiscountUsageDTO;

import java.util.List;

public interface AdminDiscountAppService {

    List<DiscountUsageDTO> searchDiscounts(String code, Long eventId, Boolean isActive);

    DiscountUsageDTO createDiscount(CreateOrUpdateDiscountDTO dto);

    DiscountUsageDTO updateDiscount(Long id, CreateOrUpdateDiscountDTO dto);

    void deleteDiscount(Long id);
}