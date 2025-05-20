package com.vecanhac.ddd.controller.admin;

import com.vecanhac.ddd.application.dto.discount.CreateOrUpdateDiscountDTO;
import com.vecanhac.ddd.application.dto.disocuntusage.DiscountUsageDTO;
import com.vecanhac.ddd.application.mapper.DiscountMapper;
import com.vecanhac.ddd.application.service.admin.AdminDiscountAppService;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeEntity;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeRepository;
import com.vecanhac.ddd.domain.discountcodeusage.DiscountCodeUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/discounts")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminDiscountController {

    private final AdminDiscountAppService discountService;

    @GetMapping
    public List<DiscountUsageDTO> searchDiscounts(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Boolean active) {
        return discountService.searchDiscounts(code, eventId, active);
    }

    @PostMapping
    public DiscountUsageDTO create(@RequestBody CreateOrUpdateDiscountDTO dto) {
        return discountService.createDiscount(dto);
    }

    @PatchMapping("/{id}")
    public DiscountUsageDTO update(@PathVariable Long id, @RequestBody CreateOrUpdateDiscountDTO dto) {
        return discountService.updateDiscount(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        discountService.deleteDiscount(id);
    }
}