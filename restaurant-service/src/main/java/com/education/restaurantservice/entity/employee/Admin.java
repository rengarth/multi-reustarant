package com.education.restaurantservice.entity.employee;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "administrators")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends Employee {
}
