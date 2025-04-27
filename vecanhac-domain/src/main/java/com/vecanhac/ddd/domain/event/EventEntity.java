package com.vecanhac.ddd.domain.event;


import com.vecanhac.ddd.domain.ticket.TicketEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
@DynamicInsert
@DynamicUpdate
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, unique = true, length = 255)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String coverImageUrl;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String venue;

    private String address;

    private String status;

    private LocalDateTime createdAt;

    private Long organizerId;

    private Long locationId;
}
