package com.vecanhac.ddd.controller.admin;

import com.vecanhac.ddd.application.dto.discount.CreateOrUpdateDiscountDTO;
import com.vecanhac.ddd.application.dto.disocuntusage.DiscountUsageDTO;
import com.vecanhac.ddd.application.service.admin.AdminDiscountAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/discounts")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminDiscountController {

    private final AdminDiscountAppService discountService;

    @GetMapping
    public List<DiscountUsageDTO> searchDiscounts(
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "eventId", required = false) Long eventId,
            @RequestParam(name = "active", required = false) Boolean active) {
        return discountService.searchDiscounts(code, eventId, active);
    }

    @PostMapping
    public DiscountUsageDTO create(@RequestBody CreateOrUpdateDiscountDTO dto) {
        return discountService.createDiscount(dto);
    }

    @PatchMapping("/{id}")
    public DiscountUsageDTO update(
            @PathVariable(name = "id") Long id, @RequestBody CreateOrUpdateDiscountDTO dto) {
        return discountService.updateDiscount(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        discountService.deleteDiscount(id);
    }
}