package com.education.restaurantservice.service.menu;

import com.education.kafkadto.dto.menu.DishDTO;
import com.education.restaurantservice.entity.menu.Dish;
import com.education.restaurantservice.exception.menu.DishIsDeletedException;
import com.education.restaurantservice.exception.menu.DishIsUnavailableException;
import com.education.restaurantservice.exception.menu.DishNotFoundException;
import com.education.restaurantservice.repository.menu.DishRepository;
import com.education.restaurantservice.util.MenuUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    public List<DishDTO> getAllDishes() {
        log.info("Fetching all dishes...");
        return dishRepository.findAll().stream()
                .map(MenuUtils::convertDishToDishDTO)
                .collect(Collectors.toList());
    }

    public List<DishDTO> getAllActiveDishes() {
        log.info("Fetching all active dishes...");
        return dishRepository.findAll().stream()
                .filter(dish -> !dish.getIsDeleted())
                .filter(Dish::getIsAvailable)
                .map(MenuUtils::convertDishToDishDTO)
                .collect(Collectors.toList());
    }

    public DishDTO getDishById(Long id) {
        log.info("Fetching dish with ID: {}...", id);
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Dish with ID: {} not found", id);
                    return new DishNotFoundException("Dish with id: " + id + " not found");
                });

        if (dish.getIsDeleted()) {
            log.error("Dish with ID: {} is deleted", id);
            throw new DishIsDeletedException("Dish is deleted");
        }

        if (!dish.getIsAvailable()) {
            log.error("Dish with ID: {} is unavailable", id);
            throw new DishIsUnavailableException("Dish is unavailable");
        }

        log.info("Dish with ID: {} found and available", id);
        return MenuUtils.convertDishToDishDTO(dish);
    }
}

