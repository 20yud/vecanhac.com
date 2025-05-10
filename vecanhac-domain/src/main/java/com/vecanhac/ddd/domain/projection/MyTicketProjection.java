package com.vecanhac.ddd.domain.projection;

import com.vecanhac.ddd.domain.model.enums.OrderStatus;
import java.math.BigDecimal;

public interface MyTicketProjection {
    Long getOrderId();
    String getEventTitle();
    String getShowingTime();
    String getTicketName();
    int getQuantity();
    BigDecimal getTotalPrice();
    OrderStatus getOrderStatus();
    String getCoverImageUrl();     // mới
    String getEventAddress();
}