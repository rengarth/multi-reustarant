package com.education.waiter.dto.worker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChangeEmployeeDataRequestDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
