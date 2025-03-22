package com.education.waiter.dto.worker;

import com.education.waiter.dto.order.OrderDTO;
import lombok.Data;

import java.util.List;

@Data
public class WaiterDTO {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean isDeleted;
    private List<OrderDTO> orders;
}
