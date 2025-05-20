package com.vecanhac.ddd.domain.discountcode;

import com.vecanhac.ddd.domain.event.EventEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discount_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "percentage", nullable = false)
    private int percentage = 0;

    @Column(name = "fixed_amount")
    private BigDecimal fixedAmount;

    @Column(name = "min_quantity", nullable = false)
    private int minQuantity = 1;

    @Column(name = "max_discount_amount")
    private BigDecimal maxDiscountAmount;

    @Column(name = "usage_limit")
    private Integer usageLimit; // null = không giới hạn

    @Column(name = "usage_limit_per_user")
    private Integer usageLimitPerUser; // null = không giới hạn

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicable_event_id")
    private EventEntity applicableEvent; // null = toàn hệ thống

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}