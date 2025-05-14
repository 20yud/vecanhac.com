package com.vecanhac.ddd.application.mapper;

import com.vecanhac.ddd.application.dto.ticket.TicketDTO;
import com.vecanhac.ddd.domain.ticket.TicketEntity;

public class TicketMapper {
    public static TicketDTO toDTO(TicketEntity entity) {
        TicketDTO dto = new TicketDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setOriginalPrice(null); // nếu có field này trong DB thì thêm vào entity luôn
        dto.setQuantityTotal(entity.getQuantityTotal());
        dto.setQuantitySold(entity.getQuantitySold());
        dto.setSaleStart(entity.getSaleStart());
        dto.setSaleEnd(entity.getSaleEnd());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }
}