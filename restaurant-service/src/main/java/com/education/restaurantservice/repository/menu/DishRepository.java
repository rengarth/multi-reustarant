package com.education.restaurantservice.repository.menu;

import com.education.restaurantservice.entity.menu.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
