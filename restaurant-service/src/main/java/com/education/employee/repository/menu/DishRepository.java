package com.education.employee.repository.menu;

import com.education.employee.entity.menu.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
