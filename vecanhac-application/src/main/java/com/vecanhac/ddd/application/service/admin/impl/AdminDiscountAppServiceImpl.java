package com.vecanhac.ddd.application.service.admin.impl;

import com.vecanhac.ddd.application.dto.discount.CreateOrUpdateDiscountDTO;
import com.vecanhac.ddd.application.dto.disocuntusage.DiscountUsageDTO;
import com.vecanhac.ddd.application.mapper.DiscountMapper;
import com.vecanhac.ddd.application.service.admin.AdminDiscountAppService;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeEntity;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeRepository;
import com.vecanhac.ddd.domain.discountcodeusage.DiscountCodeUsageRepository;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDiscountAppServiceImpl implements AdminDiscountAppService {

    private final DiscountCodeRepository discountRepo;
    private final DiscountCodeUsageRepository usageRepo;
    private final EventRepository eventRepo;

    @Override
    public List<DiscountUsageDTO> searchDiscounts(String code, Long eventId, Boolean isActive) {
        return discountRepo.findAll().stream()
                .filter(d -> code == null || d.getCode().toLowerCase().contains(code.toLowerCase()))
                .filter(d -> eventId == null || (d.getApplicableEvent() != null && d.getApplicableEvent().getId().equals(eventId)))
                .filter(d -> isActive == null || d.isActive() == isActive)
                .map(d -> DiscountMapper.toUsageDTO(d, usageRepo.countByDiscountCode_IdAndIsSuccessfulTrue(d.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public DiscountUsageDTO createDiscount(CreateOrUpdateDiscountDTO dto) {
        DiscountCodeEntity discount = new DiscountCodeEntity();
        applyFields(discount, dto);
        discount.setCreatedAt(LocalDateTime.now());
        return DiscountMapper.toUsageDTO(discountRepo.save(discount), 0);
    }

    @Override
    public DiscountUsageDTO updateDiscount(Long id, CreateOrUpdateDiscountDTO dto) {
        DiscountCodeEntity discount = discountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mã giảm giá"));
        applyFields(discount, dto);
        return DiscountMapper.toUsageDTO(discountRepo.save(discount),
                usageRepo.countByDiscountCode_IdAndIsSuccessfulTrue(id));
    }

    @Override
    public void deleteDiscount(Long id) {
        DiscountCodeEntity discount = discountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mã"));
        discount.setActive(false); // không xóa DB, chỉ vô hiệu
        discountRepo.save(discount);
    }

    private void applyFields(DiscountCodeEntity discount, CreateOrUpdateDiscountDTO dto) {
        discount.setCode(dto.getCode());
        discount.setPercentage(dto.getPercentage() != null ? dto.getPercentage() : 0);
        discount.setFixedAmount(dto.getFixedAmount());
        discount.setMinQuantity(dto.getMinQuantity());
        discount.setMaxDiscountAmount(dto.getMaxDiscountAmount());
        discount.setUsageLimit(dto.getUsageLimit());
        discount.setUsageLimitPerUser(dto.getUsageLimitPerUser());
        discount.setExpiresAt(dto.getExpiresAt());
        discount.setActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        if (dto.getApplicableEventId() != null) {
            EventEntity event = eventRepo.findById(dto.getApplicableEventId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));
            discount.setApplicableEvent(event);
        } else {
            discount.setApplicableEvent(null);
        }
    }
}