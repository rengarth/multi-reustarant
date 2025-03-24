package com.education.restaurantservice.dto.employee;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "password")
public class WaiterRegistrationDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
}
