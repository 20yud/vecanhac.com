package com.vecanhac.ddd.application.dto.discount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class DiscountResponseDTO {
    private boolean valid;
    private BigDecimal discountAmount;
    private String message;

}