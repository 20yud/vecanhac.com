package com.vecanhac.ddd.application.dto.discount;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DiscountRequestDTO {
    private String code;
    private int totalQuantity;
    private BigDecimal totalPrice;
    private Long eventId; // cần để kiểm tra applicable_event_id
}