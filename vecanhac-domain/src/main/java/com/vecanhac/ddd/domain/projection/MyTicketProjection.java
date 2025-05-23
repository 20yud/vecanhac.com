package com.vecanhac.ddd.domain.projection;

import com.vecanhac.ddd.domain.model.enums.OrderStatus;
import java.math.BigDecimal;

public interface MyTicketProjection {
    Long getOrderItemId(); // 👈 Thêm dòng này
    Long getOrderId();
    String getEventTitle();
    String getShowingTime();
    String getTicketName();
    int getQuantity();
    BigDecimal getTotalPrice();
    OrderStatus getOrderStatus();
    String getCoverImageUrl();
    String getEventAddress();
}
