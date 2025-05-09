package com.vecanhac.ddd.application.service.ticket.impl;

import com.vecanhac.ddd.application.dto.ticket.MyTicketDTO;
import com.vecanhac.ddd.application.service.ticket.TicketAppService;
import com.vecanhac.ddd.application.service.ticket.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketAppServiceImpl implements TicketAppService {

    private final OrderQueryRepository orderQueryRepository;

    @Override
    public List<MyTicketDTO> getMyTickets(Long userId) {
        return orderQueryRepository.findMyTicketsByUserId(userId);
    }
}