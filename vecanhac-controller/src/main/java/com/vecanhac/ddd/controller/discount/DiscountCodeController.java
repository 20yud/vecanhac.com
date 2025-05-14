package com.vecanhac.ddd.controller.discount;

import com.vecanhac.ddd.application.dto.discount.DiscountRequestDTO;
import com.vecanhac.ddd.application.dto.discount.DiscountResponseDTO;
import com.vecanhac.ddd.application.service.discount.DiscountCodeAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountCodeController {

    private final DiscountCodeAppService discountCodeAppService;

    @PostMapping("/check")
    public ResponseEntity<DiscountResponseDTO> checkDiscount(@RequestBody DiscountRequestDTO request) {
        return ResponseEntity.ok(discountCodeAppService.checkDiscount(request));
    }
}