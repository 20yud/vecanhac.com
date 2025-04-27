package com.vecanhac.ddd.domain.ticket;


import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.showing.ShowingEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tickets")
@DynamicInsert
@DynamicUpdate
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private Integer quantityTotal;

    private Integer quantitySold;

    private LocalDateTime saleStart;

    private LocalDateTime saleEnd;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showing_id")
    private ShowingEntity showing;  // ✅ Đúng thực tế: ticket → showing → event
}