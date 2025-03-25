package com.education.restaurantservice.dto.employee;

import com.education.restaurantservice.dto.order.OrderDTO;
import lombok.Data;

import java.util.List;

@Data
public class WaiterDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean isDeleted;
    private List<OrderDTO> orders;
}
