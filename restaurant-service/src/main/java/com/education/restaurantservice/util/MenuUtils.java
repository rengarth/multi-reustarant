package com.education.restaurantservice.util;

import com.education.kafkadto.dto.menu.CategoryDTO;
import com.education.kafkadto.dto.menu.DishDTO;
import com.education.restaurantservice.entity.menu.Category;
import com.education.restaurantservice.entity.menu.Dish;

public class MenuUtils {

    public static CategoryDTO convertCategoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setIsDeleted(category.getIsDeleted());
        if (category.getParent() != null) {
            categoryDTO.setParentId(category.getParent().getId());
        }
        return categoryDTO;
    }

    public static DishDTO convertDishToDishDTO(Dish dish) {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setId(dish.getId());
        dishDTO.setName(dish.getName());
        dishDTO.setDescription(dish.getDescription());
        dishDTO.setPricePerOne(dish.getPricePerOne());
        dishDTO.setIsDeleted(dish.getIsDeleted());
        dishDTO.setIsAvailable(dish.getIsAvailable());
        CategoryDTO categoryDTO = convertCategoryToCategoryDTO(dish.getCategory());
        dishDTO.setCategoryName(categoryDTO.getName());
        return dishDTO;
    }
}
