package com.education.kitchen.entity.dish;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dish_name")
    private String name;

    @Column(name = "cooking_time_in_minutes")
    private int cookingTimeMinutes;

    @Column(name = "stock_quantity")
    private int stockQuantity;
}
