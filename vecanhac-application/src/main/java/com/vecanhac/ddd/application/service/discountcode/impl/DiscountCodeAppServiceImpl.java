package com.vecanhac.ddd.application.service.discountcode.impl;

import com.vecanhac.ddd.application.dto.discount.DiscountRequestDTO;
import com.vecanhac.ddd.application.dto.discount.DiscountResponseDTO;
import com.vecanhac.ddd.application.service.discountcode.DiscountCodeAppService;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeEntity;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeRepository;
import com.vecanhac.ddd.domain.discountcodeusage.DiscountCodeUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiscountCodeAppServiceImpl implements DiscountCodeAppService {

    private final DiscountCodeRepository discountCodeRepository;
    private final DiscountCodeUsageRepository usageRepository;

    @Override
    public DiscountResponseDTO checkDiscount(DiscountRequestDTO request, Long userId) {
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

        // ✅ Nếu có giới hạn theo event
        if (code.getApplicableEvent() != null && !code.getApplicableEvent().getId().equals(request.getEventId())) {
            return new DiscountResponseDTO(false, BigDecimal.ZERO, "Mã chỉ áp dụng cho một sự kiện khác");
        }

        // ✅ Kiểm tra số lần dùng toàn hệ thống
        if (code.getUsageLimit() != null) {
            long used = usageRepository.countTotalSuccessfulUsage(code.getId());
            if (used >= code.getUsageLimit()) {
                return new DiscountResponseDTO(false, BigDecimal.ZERO, "Mã đã hết lượt sử dụng");
            }
        }

        // ✅ Kiểm tra số lần dùng của user
        if (code.getUsageLimitPerUser() != null) {
            long usedByUser = usageRepository.countSuccessfulUsageByUser(code.getId(), userId);
            if (usedByUser >= code.getUsageLimitPerUser()) {
                return new DiscountResponseDTO(false, BigDecimal.ZERO, "Bạn đã dùng mã này tối đa số lần cho phép");
            }
        }

        // ✅ Tính số tiền giảm
        BigDecimal discountAmount = BigDecimal.ZERO;

        if (code.getFixedAmount() != null) {
            discountAmount = code.getFixedAmount();
        } else {
            BigDecimal raw = request.getTotalPrice()
                    .multiply(BigDecimal.valueOf(code.getPercentage()))
                    .divide(BigDecimal.valueOf(100));
            discountAmount = code.getMaxDiscountAmount() != null
                    ? raw.min(code.getMaxDiscountAmount())
                    : raw;
        }

        return new DiscountResponseDTO(true, discountAmount, "Áp mã thành công");
    }

}