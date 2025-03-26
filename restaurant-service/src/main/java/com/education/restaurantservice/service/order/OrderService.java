package com.education.restaurantservice.service.order;

import com.education.restaurantservice.dto.order.OrderDTO;
import com.education.restaurantservice.entity.menu.Dish;
import com.education.restaurantservice.entity.order.Order;
import com.education.restaurantservice.entity.order.OrderDetail;
import com.education.restaurantservice.entity.order.OrderStatus;
import com.education.restaurantservice.entity.order.PaymentStatus;
import com.education.restaurantservice.entity.table.RestTable;
import com.education.restaurantservice.entity.table.RestTableItem;
import com.education.restaurantservice.repository.menu.DishRepository;
import com.education.restaurantservice.repository.order.OrderRepository;
import com.education.restaurantservice.service.employee.WaiterService;
import com.education.restaurantservice.service.table.RestTableService;
import com.education.restaurantservice.util.OrderUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final WaiterService waiterService;
    private final RestTableService restTableService;

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
        return OrderUtils.convertOrderToOrderDTO(order);
    }
}
