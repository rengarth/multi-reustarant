package com.education.waiter.entity.order;

import com.education.waiter.entity.waiter.Waiter;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "waiter_id", nullable = false)
    private Waiter waiter;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.COOKING;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "lead_time")
    private LocalDateTime leadTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }
}


