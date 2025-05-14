package com.vecanhac.ddd.application.service.discount.impl;

import com.vecanhac.ddd.application.dto.discount.DiscountRequestDTO;
import com.vecanhac.ddd.application.dto.discount.DiscountResponseDTO;
import com.vecanhac.ddd.application.service.discount.DiscountCodeAppService;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeEntity;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiscountCodeAppServiceImpl implements DiscountCodeAppService {

    private final DiscountCodeRepository discountCodeRepository;

    @Override
    public DiscountResponseDTO checkDiscount(DiscountRequestDTO request) {
        DiscountCodeEntity code = discountCodeRepository.findByCodeAndIsActiveTrue(request.getCode())
                .orElse(null);

        if (code == null) {
            return new DiscountResponseDTO(false, BigDecimal.ZERO, "Mã không hợp lệ hoặc không tồn tại");
        }

        if (code.getExpiresAt() != null && code.getExpiresAt().isBefore(LocalDateTime.now())) {
            return new DiscountResponseDTO(false, BigDecimal.ZERO, "Mã đã hết hạn");
        }

        if (request.getTotalQuantity() < code.getMinQuantity()) {
            return new DiscountResponseDTO(false, BigDecimal.ZERO, "Số lượng vé chưa đủ để áp mã");
        }

        BigDecimal rawDiscount = request.getTotalPrice()
                .multiply(BigDecimal.valueOf(code.getPercentage()))
                .divide(BigDecimal.valueOf(100));

        BigDecimal discountAmount = code.getMaxDiscountAmount() != null
                ? rawDiscount.min(code.getMaxDiscountAmount())
                : rawDiscount;

        return new DiscountResponseDTO(true, discountAmount, "Áp mã thành công");
    }
}