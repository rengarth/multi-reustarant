package com.education.employee.entity.employee;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "administrators")
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends Employee {
}
