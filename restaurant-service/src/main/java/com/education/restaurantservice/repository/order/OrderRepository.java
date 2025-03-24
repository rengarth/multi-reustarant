package com.education.restaurantservice.repository.order;

import com.education.restaurantservice.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
