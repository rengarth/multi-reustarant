package com.education.employee.dto.worker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChangePasswordRequestDTO {
    private String oldPassword;
    private String newPassword;
}
