package com.vecanhac.ddd.application.service.ticket;

import com.vecanhac.ddd.application.dto.ticket.MyTicketDTO;
import java.util.List;

public interface OrderRepositoryCustom {
    List<MyTicketDTO> findMyTicketsByUserId(Long userId);
}