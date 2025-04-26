package com.vecanhac.ddd.domain.event;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
@DynamicInsert
@DynamicUpdate
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "varchar(255) comment 'title'", nullable = false)
    private String title;

    @Column(columnDefinition = "varchar(255) comment 'title'", nullable = false, unique = true)
    private String slug;

    private String description;
    private String coverImageUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String venue;
    private Long categoryId;
    private String status;


}



