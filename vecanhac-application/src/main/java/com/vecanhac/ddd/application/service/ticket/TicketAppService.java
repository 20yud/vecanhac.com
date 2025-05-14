package com.vecanhac.ddd.application.service.ticket;


import com.vecanhac.ddd.application.dto.order.CreateOrderRequestDTO;
import com.vecanhac.ddd.application.dto.order.MyOrderDTO;
import com.vecanhac.ddd.application.dto.order.OrderItemInOrderDTO;
import com.vecanhac.ddd.domain.projection.MyTicketProjection;

import java.util.List;

public interface TicketAppService {
    List<MyOrderDTO> getMyOrders(Long userId, String statusParam, String filterParam);
    Long createOrder(CreateOrderRequestDTO request);
    void cancelOrder(Long orderId, Long userId);
    void cancelOrderItem(Long orderItemId, Long userId);
    List<OrderItemInOrderDTO> getOrderItemsByOrder(Long orderId, Long userId);
}