package com.education.restaurantservice.service.menu;

import com.education.restaurantservice.dto.menu.DishDTO;
import com.education.restaurantservice.entity.menu.Dish;
import com.education.restaurantservice.exception.menu.DishIsDeletedException;
import com.education.restaurantservice.exception.menu.DishIsUnavailableException;
import com.education.restaurantservice.exception.menu.DishNotFoundException;
import com.education.restaurantservice.repository.menu.DishRepository;
import com.education.restaurantservice.util.MenuUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    public List<DishDTO> getAllDishes() {
        return dishRepository.findAll().stream()
                .map(MenuUtils::convertDishToDishDTO)
                .collect(Collectors.toList());
    }

    public List<DishDTO> getAllActiveDishes() {
        return dishRepository.findAll().stream()
                .filter(dish -> !dish.getIsDeleted())
                .filter(dish -> !dish.getIsAvailable())
                .map(MenuUtils::convertDishToDishDTO)
                .collect(Collectors.toList());
    }

    public DishDTO getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException("Dish with id: " + id + " not found"));
        if (dish.getIsDeleted()) {
            throw new DishIsDeletedException("Dish is deleted");
        }
        if (!dish.getIsAvailable()) {
            throw new DishIsUnavailableException("Dish is unavailable");
        }
        return MenuUtils.convertDishToDishDTO(dish);
    }
}
