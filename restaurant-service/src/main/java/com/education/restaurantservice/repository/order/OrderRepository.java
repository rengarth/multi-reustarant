package com.education.restaurantservice.repository.order;

import com.education.restaurantservice.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByWaiterId(Long waiterId);
    List<Order> findByTableNumberAndWaiterId(Integer tableNumber, Long waiterId);
}
