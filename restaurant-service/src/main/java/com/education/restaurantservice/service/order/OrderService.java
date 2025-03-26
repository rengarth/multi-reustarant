package com.education.restaurantservice.service.order;

import com.education.kafkadto.dto.order.OrderDTO;
import com.education.kafkadto.dto.order.OrderStatus;
import com.education.kafkadto.dto.order.PaymentStatus;
import com.education.restaurantservice.entity.employee.Waiter;
import com.education.restaurantservice.entity.menu.Dish;
import com.education.restaurantservice.entity.order.Order;
import com.education.restaurantservice.entity.order.OrderDetail;
import com.education.restaurantservice.entity.table.RestTable;
import com.education.restaurantservice.entity.table.RestTableItem;
import com.education.restaurantservice.exception.order.OrderNotFoundException;
import com.education.restaurantservice.repository.menu.DishRepository;
import com.education.restaurantservice.repository.order.OrderRepository;
import com.education.restaurantservice.repository.table.RestTableRepository;
import com.education.restaurantservice.service.employee.WaiterService;
import com.education.restaurantservice.service.table.RestTableService;
import com.education.restaurantservice.util.OrderUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final RestTableRepository restTableRepository;
    private final WaiterService waiterService;
    private final RestTableService restTableService;
    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;

    @Transactional
    public OrderDTO createTableOrder(Integer tableNumber) {
        Order order = new Order();
        RestTable table = restTableService.getTableOfCurrentWaiter(tableNumber);
        if (table.getTableItems().isEmpty()) {
            throw new IllegalArgumentException("No items found on table: " + tableNumber);
        }
        order.setWaiter(waiterService.getCurrentWaiter());
        order.setStatus(OrderStatus.CREATED);
        order.setPaymentStatus(PaymentStatus.NOT_PAID);
        order.setTotalAmount(table.getTotalAmount());
        order.setCreateTime(LocalDateTime.now());
        order.setLeadTime(null);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (RestTableItem restTableItem : table.getTableItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setDish(restTableItem.getDish());
            orderDetail.setQuantity(restTableItem.getQuantity());
            orderDetail.setTotalAmount(restTableItem.getTotalPrice());
            orderDetails.add(orderDetail);
        }
        order.setOrderDetails(orderDetails);
        orderRepository.save(order);
        table.getTableItems().forEach(tableItem -> {
            Dish dish = tableItem.getDish();
            int remainingStock = dish.getStockQuantity() - tableItem.getQuantity();
            if (remainingStock == 0) {
                dish.setIsAvailable(false);
            }
            dish.setStockQuantity(remainingStock);
            dishRepository.save(dish);
        });
        restTableService.clearTable(tableNumber);

        OrderDTO orderDTO = OrderUtils.convertOrderToOrderDTO(order);
        kafkaTemplate.send("order-ready-for-payment", orderDTO);
        return orderDTO;
    }

    @Transactional
    public void retryPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        Waiter currentWaiter = waiterService.getCurrentWaiter();
        if (!order.getWaiter().getId().equals(currentWaiter.getId())) {
            throw new IllegalArgumentException("You can only retry payment for your own orders.");
        }
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalArgumentException("Order is already paid.");
        }

        OrderDTO orderDTO = OrderUtils.convertOrderToOrderDTO(order);
        kafkaTemplate.send("order-ready-for-payment", orderDTO);
    }

    @Transactional
    public OrderDTO serveOrderToTable(Long orderId, Integer tableNumber) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        if (order.getPaymentStatus().equals(PaymentStatus.PAID)
                && order.getStatus().equals(OrderStatus.READY)
                && order.getWaiter().getId().equals(waiterService.getCurrentWaiter().getId())
                && !order.getOrderDetails().isEmpty()) {
            order.setStatus(OrderStatus.SERVED);
            orderRepository.save(order);
            RestTable table = restTableRepository.findByNumber(tableNumber)
                    .orElseThrow(() -> new OrderNotFoundException("Table not found with number: " + tableNumber));
            table.setWaiter(null);
            restTableRepository.save(table);
            return OrderUtils.convertOrderToOrderDTO(order);
        }
        else throw new IllegalArgumentException("Order is not ready to be served");
    }
}
