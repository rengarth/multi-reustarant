package com.education.restaurantservice.service.menu;

import com.education.kafkadto.dto.menu.CategoryDTO;
import com.education.kafkadto.dto.menu.DishDTO;
import com.education.restaurantservice.entity.menu.Category;
import com.education.restaurantservice.exception.menu.CategoryNotFoundException;
import com.education.restaurantservice.repository.menu.CategoryRepository;
import com.education.restaurantservice.util.MenuUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        log.info("Fetching all categories...");
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(MenuUtils::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        log.info("Fetching category with ID: {}...", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category with ID: {} not found", id);
                    return new CategoryNotFoundException("Category with id: " + id + " not found");
                });
        log.info("Category with ID: {} found", id);
        return MenuUtils.convertCategoryToCategoryDTO(category);
    }

    public List<DishDTO> getCategoryDishes(Long id) {
        log.info("Fetching dishes for category with ID: {}...", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category with ID: {} not found", id);
                    return new CategoryNotFoundException("Category with id: " + id + " not found");
                });

        List<DishDTO> dishDTOs = category.getDishes().stream()
                .map(MenuUtils::convertDishToDishDTO)
                .collect(Collectors.toList());
        log.info("Found {} dishes for category with ID: {}", dishDTOs.size(), id);
        return dishDTOs;
    }
}

