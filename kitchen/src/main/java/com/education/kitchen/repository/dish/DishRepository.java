package com.education.kitchen.repository.dish;

import com.education.kitchen.entity.dish.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
