package com.vecanhac.ddd.controller.discount;

import com.vecanhac.ddd.application.dto.discount.DiscountRequestDTO;
import com.vecanhac.ddd.application.dto.discount.DiscountResponseDTO;
import com.vecanhac.ddd.application.service.discountcode.DiscountCodeAppService;
import com.vecanhac.ddd.domain.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountCodeController {

    private final DiscountCodeAppService discountCodeAppService;

    @PostMapping("/check")
    public ResponseEntity<DiscountResponseDTO> checkDiscount(
            @RequestBody DiscountRequestDTO request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(discountCodeAppService.checkDiscount(request, user.getId()));
    }

}