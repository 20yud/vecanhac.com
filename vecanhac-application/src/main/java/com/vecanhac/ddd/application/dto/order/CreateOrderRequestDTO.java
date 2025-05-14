package com.vecanhac.ddd.application.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequestDTO {
    private Long userId;
    private List<TicketItem> tickets;
    private String discountCode;

    @Data
    public static class TicketItem {
        private Long ticketId;
        private int quantity;
    }
}
