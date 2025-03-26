package com.education.restaurantservice.entity.order;

import com.education.kafkadto.dto.order.OrderStatus;
import com.education.kafkadto.dto.order.PaymentStatus;
import com.education.restaurantservice.entity.employee.Waiter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "waiter_id", nullable = false)
    private Waiter waiter;

    @Column(name = "table_number")
    private Integer tableNumber;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.NOT_PAID;

    @Column(name = "total_amount")
    private int totalAmount;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "lead_time")
    private LocalDateTime leadTime;
}


