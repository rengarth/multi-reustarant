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
import com.education.restaurantservice.exception.table.RestTableIsNotAssignedException;
import com.education.restaurantservice.exception.table.RestTableNotFoundException;
import com.education.restaurantservice.repository.menu.DishRepository;
import com.education.restaurantservice.repository.order.OrderRepository;
import com.education.restaurantservice.repository.table.RestTableRepository;
import com.education.restaurantservice.service.employee.WaiterService;
import com.education.restaurantservice.service.table.RestTableService;
import com.education.restaurantservice.util.OrderUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        log.info("Creating order for table number: {}...", tableNumber);

        Order order = new Order();
        RestTable table = restTableService.getTableOfCurrentWaiter(tableNumber);
        if (table.getTableItems().isEmpty()) {
            log.error("No items found on table: {}", tableNumber);
            throw new IllegalArgumentException("No items found on table: " + tableNumber);
        }

        order.setWaiter(waiterService.getCurrentWaiter());
        order.setTableNumber(tableNumber);
        order.setStatus(OrderStatus.CREATED);
        order.setPaymentStatus(PaymentStatus.NOT_PAID);
        order.setTotalAmount(table.getTotalAmount());
        order.setCreateTime(LocalDateTime.now());
        order.setLeadTime(null);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (RestTableItem restTableItem : table.getTableItems()) {
            log.info("Adding order detail for dish: {} with quantity: {}...", restTableItem.getDish().getName(), restTableItem.getQuantity());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setDish(restTableItem.getDish());
            orderDetail.setQuantity(restTableItem.getQuantity());
            orderDetail.setTotalAmount(restTableItem.getTotalPrice());
            orderDetails.add(orderDetail);
        }
        order.setOrderDetails(orderDetails);

        log.info("Saving order for table number: {}...", tableNumber);
        orderRepository.save(order);

        table.getTableItems().forEach(tableItem -> {
            Dish dish = tableItem.getDish();
            int remainingStock = dish.getStockQuantity() - tableItem.getQuantity();
            if (remainingStock == 0) {
                log.info("Dish {} is out of stock, setting availability to false...", dish.getName());
                dish.setIsAvailable(false);
            }
            dish.setStockQuantity(remainingStock);
            log.info("Updating stock quantity for dish {}: {}", dish.getName(), remainingStock);
            dishRepository.save(dish);
        });

        restTableService.clearTable(tableNumber);

        OrderDTO orderDTO = OrderUtils.convertOrderToOrderDTO(order);
        log.info("Sending order to Kafka for payment processing...");
        kafkaTemplate.send("order-ready-for-payment", orderDTO);
        return orderDTO;
    }

    @Transactional
    public void retryPayment(Long orderId) {
        log.info("Retrying payment for order ID: {}...", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with id: {}", orderId);
                    return new OrderNotFoundException("Order not found with id: " + orderId);
                });

        Waiter currentWaiter = waiterService.getCurrentWaiter();
        if (!order.getWaiter().getId().equals(currentWaiter.getId())) {
            log.error("Waiter {} is attempting to retry payment for an order not assigned to them.", currentWaiter.getId());
            throw new IllegalArgumentException("You can only retry payment for your own orders.");
        }
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            log.error("Order ID: {} has already been paid.", orderId);
            throw new IllegalArgumentException("Order is already paid.");
        }

        OrderDTO orderDTO = OrderUtils.convertOrderToOrderDTO(order);
        log.info("Sending retry payment request for order ID: {} to Kafka...", orderId);
        kafkaTemplate.send("order-ready-for-payment", orderDTO);
    }

    @Transactional
    public OrderDTO serveOrderToTable(Long orderId) {
        log.info("Serving order to table for order ID: {}...", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with id: {}", orderId);
                    return new OrderNotFoundException("Order not found with id: " + orderId);
                });

        Integer tableNumber = order.getTableNumber();
        Waiter currentWaiter = waiterService.getCurrentWaiter();

        RestTable table = restTableRepository.findByNumber(tableNumber)
                .orElseThrow(() -> {
                    log.error("Table with number {} not found.", tableNumber);
                    return new RestTableNotFoundException("Table not found with number: " + tableNumber);
                });

        if (table.getWaiter() == null || !table.getWaiter().getId().equals(currentWaiter.getId())) {
            log.error("Table {} is no longer assigned to the current waiter.", tableNumber);
            throw new RestTableIsNotAssignedException("Table is no longer assigned to the current waiter.");
        }

        if (order.getPaymentStatus() == PaymentStatus.PAID
                && order.getStatus() == OrderStatus.READY
                && order.getWaiter().getId().equals(currentWaiter.getId())
                && order.getOrderDetails() != null
                && !order.getOrderDetails().isEmpty()) {

            log.info("Updating order status to SERVED for order ID: {}", orderId);
            order.setStatus(OrderStatus.SERVED);
            orderRepository.save(order);

            List<Order> ordersForTable = orderRepository.findByTableNumberAndWaiterId(tableNumber, currentWaiter.getId());
            ordersForTable.removeIf(o -> o.getId().equals(order.getId()));
            boolean hasActiveOrders = ordersForTable.stream()
                    .anyMatch(o -> o.getStatus() != OrderStatus.SERVED && o.getStatus() != OrderStatus.CANCELLED);

            if (!hasActiveOrders) {
                log.info("No active orders left, unassigning waiter from table {}...", tableNumber);
                table.setWaiter(null);
            }

            restTableRepository.save(table);
            return OrderUtils.convertOrderToOrderDTO(order);
        } else {
            log.error("Order ID: {} is not ready to be served.", orderId);
            throw new IllegalArgumentException("Order is not ready to be served");
        }
    }
}


