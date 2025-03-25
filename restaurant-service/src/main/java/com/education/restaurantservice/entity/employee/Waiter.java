package com.education.restaurantservice.entity.employee;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "waiters")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Waiter extends Employee {
}
