package com.vecanhac.ddd.domain.order;

import com.vecanhac.ddd.domain.model.enums.OrderStatus;
import com.vecanhac.ddd.domain.orderitem.OrderItemEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Sửa lại: dùng userId dạng primitive, không dùng @ManyToOne
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "discount_code")
    private String discountCode;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Enumerated(EnumType.STRING) // quan trọng
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}