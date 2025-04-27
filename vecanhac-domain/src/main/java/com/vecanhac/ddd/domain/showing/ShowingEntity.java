package com.vecanhac.ddd.domain.showing;

import com.vecanhac.ddd.domain.event.EventEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "showings")
@DynamicInsert
@DynamicUpdate
public class ShowingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long seatMapId;

    private Boolean isEnabledQueue;


}