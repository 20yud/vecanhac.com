package com.vecanhac.ddd.application.dto.order;

import com.vecanhac.ddd.domain.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyOrderDTO {
    private Long orderId;
    private String eventTitle;
    private String eventAddress;
    private String coverImageUrl;
    private String showingTime;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
}