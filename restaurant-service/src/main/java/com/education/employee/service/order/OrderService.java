package com.education.employee.service.order;

import com.education.employee.dto.order.OrderDTO;
import com.education.employee.entity.order.Order;
import com.education.employee.entity.order.OrderDetail;
import com.education.employee.entity.order.OrderStatus;
import com.education.employee.entity.order.PaymentStatus;
import com.education.employee.entity.table.RestTable;
import com.education.employee.entity.table.RestTableItem;
import com.education.employee.repository.order.OrderRepository;
import com.education.employee.service.employee.WaiterService;
import com.education.employee.service.table.RestTableService;
import com.education.employee.util.OrderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WaiterService waiterService;
    private final RestTableService restTableService;

    public OrderDTO createTableOrder(Integer number) {
        Order order = new Order();
        RestTable table = restTableService.getTableOfCurrentWaiter(number);
        if (table.getTableItems().isEmpty()) {
            throw new IllegalArgumentException("No items found on table: " + number);
        }
        order.setWaiter(waiterService.getCurrentWaiter());
        order.setStatus(OrderStatus.CREATED);
        order.setPaymentStatus(PaymentStatus.NOT_PAID);
        order.setTotalAmount(table.getTotalAmount());
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
        return OrderUtils.convertOrderToOrderDTO(order);
    }
}
