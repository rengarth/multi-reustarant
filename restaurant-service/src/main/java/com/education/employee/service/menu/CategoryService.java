package com.education.employee.service.menu;

import com.education.employee.dto.menu.CategoryDTO;
import com.education.employee.dto.menu.DishDTO;
import com.education.employee.entity.menu.Category;
import com.education.employee.exception.menu.CategoryNotFoundException;
import com.education.employee.repository.menu.CategoryRepository;
import com.education.employee.util.MenuUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(MenuUtils::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found"));
        return MenuUtils.convertCategoryToCategoryDTO(category);
    }

    public List<DishDTO> getCategoryDishes(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found"));
        return category.getDishes().stream()
                .map(MenuUtils::convertDishToDishDTO)
                .collect(Collectors.toList());
    }
}
