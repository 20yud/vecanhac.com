package com.vecanhac.ddd.domain.orderitem;


import com.vecanhac.ddd.domain.order.OrderEntity;
import com.vecanhac.ddd.domain.ticket.TicketEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketEntity ticket;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "price_each", nullable = false)
    private BigDecimal priceEach;

    @Column(name = "order_item_code", nullable = false, unique = true)
    private String orderItemCode;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;

    @Column(name = "is_cancelled")
    private boolean isCancelled = false;

    @Column(name = "used_at")
    private LocalDateTime usedAt;
}
