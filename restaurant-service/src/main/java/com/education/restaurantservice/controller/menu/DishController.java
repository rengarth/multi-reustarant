package com.education.restaurantservice.controller.menu;

import com.education.kafkadto.dto.menu.DishDTO;
import com.education.restaurantservice.service.menu.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        List<DishDTO> dishes = dishService.getAllDishes();
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DishDTO>> getAllActiveDishes() {
        List<DishDTO> activeDishes = dishService.getAllActiveDishes();
        return ResponseEntity.ok(activeDishes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDTO> getDishById(@PathVariable Long id) {
        DishDTO dishDTO = dishService.getDishById(id);
        return ResponseEntity.ok(dishDTO);
    }
}

