package com.education.employee.dto.worker;

import com.education.employee.dto.order.OrderDTO;
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
