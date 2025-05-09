package com.vecanhac.ddd.application.dto.ticket;

import com.vecanhac.ddd.domain.model.enums.OrderStatus;
import lombok.Getter;

@Getter
public class MyTicketDTO {
    private Long orderId;
    private String eventTitle;
    private String showingTime;
    private String ticketName;
    private int quantity;
    private Double totalPrice;
    private OrderStatus orderStatus;

    public MyTicketDTO(Long orderId, String eventTitle, String showingTime,
                       String ticketName, int quantity, Double totalPrice,
                       OrderStatus orderStatus) {
        this.orderId = orderId;
        this.eventTitle = eventTitle;
        this.showingTime = showingTime;
        this.ticketName = ticketName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }
}
