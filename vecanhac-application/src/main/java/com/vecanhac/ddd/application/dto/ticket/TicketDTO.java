package com.vecanhac.ddd.application.dto.ticket;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TicketDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer quantityTotal;
    private Integer quantitySold;
    private LocalDateTime saleStart;
    private LocalDateTime saleEnd;
    private String status;
}