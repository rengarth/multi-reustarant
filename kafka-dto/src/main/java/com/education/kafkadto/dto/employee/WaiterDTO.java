package com.education.kafkadto.dto.employee;

import lombok.Data;

@Data
public class WaiterDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean isDeleted;
}
