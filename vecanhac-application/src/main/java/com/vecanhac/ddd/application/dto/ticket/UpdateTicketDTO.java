package com.vecanhac.ddd.application.dto.ticket;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UpdateTicketDTO {
    @NotNull
    private Long id; // bắt buộc
    private String name;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer quantityTotal;
    private LocalDateTime saleStart;
    private LocalDateTime saleEnd;
    private String color;
    private String imageUrl;
}