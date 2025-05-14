package com.vecanhac.ddd.domain.projection;

import com.vecanhac.ddd.domain.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface MyOrderProjection {
    Long getOrderId();
    String getEventTitle();
    String getEventAddress();
    String getCoverImageUrl();
    String getShowingTime();
    BigDecimal getTotalPrice();
    OrderStatus getOrderStatus();
    LocalDateTime getCreatedAt(); // thêm dòng này
}