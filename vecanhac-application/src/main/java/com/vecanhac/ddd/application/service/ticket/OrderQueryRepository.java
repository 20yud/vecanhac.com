package com.vecanhac.ddd.application.service.ticket;

import com.vecanhac.ddd.application.dto.ticket.MyTicketDTO;
import java.util.List;

public interface OrderQueryRepository {
    List<MyTicketDTO> findMyTicketsByUserId(Long userId);
}