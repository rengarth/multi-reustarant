package com.education.employee.service.menu;

import com.education.employee.dto.menu.DishDTO;
import com.education.employee.entity.menu.Dish;
import com.education.employee.exception.menu.DishIsDeletedException;
import com.education.employee.exception.menu.DishNotFoundException;
import com.education.employee.repository.menu.DishRepository;
import com.education.employee.util.MenuUtils;
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
                .map(MenuUtils::convertDishToDishDTO)
                .collect(Collectors.toList());
    }

    public DishDTO getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException("Dish with id: " + id + " not found"));
        if (dish.getIsDeleted()) {
            throw new DishIsDeletedException("Dish is deleted");
        }
        return MenuUtils.convertDishToDishDTO(dish);
    }
}
