package com.vecanhac.ddd.application.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateTicketDTO {
    @NotBlank
    private String name;
    @NotNull
    private BigDecimal price;
    private BigDecimal originalPrice;
    @NotNull private Integer quantityTotal;
    private LocalDateTime saleStart;
    private LocalDateTime saleEnd;
    private String color;
    private String imageUrl;
}
