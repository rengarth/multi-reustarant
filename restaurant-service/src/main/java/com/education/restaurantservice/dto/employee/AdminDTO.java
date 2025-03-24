package com.education.restaurantservice.dto.employee;

import lombok.Data;

@Data
public class AdminDTO {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean isDeleted;
}
