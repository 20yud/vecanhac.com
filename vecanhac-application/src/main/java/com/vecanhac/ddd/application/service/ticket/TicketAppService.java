package com.vecanhac.ddd.application.service.ticket;


import com.vecanhac.ddd.application.dto.order.CreateOrderRequestDTO;
import com.vecanhac.ddd.domain.projection.MyTicketProjection;

import java.util.List;

public interface TicketAppService {
    List<MyTicketProjection> getMyTickets(Long userId, String statusParam, String filterParam);
    Long createOrder(CreateOrderRequestDTO request);
}