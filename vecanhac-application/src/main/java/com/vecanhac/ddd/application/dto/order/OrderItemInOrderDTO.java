package com.vecanhac.ddd.application.dto.order;

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
public class OrderItemInOrderDTO {
    private String orderItemCode;
    private String qrCodeUrl;
    private Integer quantity;
    private BigDecimal priceEach;
    private Boolean isUsed;
    private Boolean isCancelled;

    private String ticketName;
    private String eventTitle;
    private String eventImage;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
