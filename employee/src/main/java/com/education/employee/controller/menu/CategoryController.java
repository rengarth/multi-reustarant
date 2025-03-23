package com.education.employee.controller.menu;

import com.education.employee.dto.menu.CategoryDTO;
import com.education.employee.dto.menu.DishDTO;
import com.education.employee.service.menu.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping("/{id}/dishes")
    public ResponseEntity<List<DishDTO>> getCategoryDishes(@PathVariable Long id) {
        List<DishDTO> dishes = categoryService.getCategoryDishes(id);
        return ResponseEntity.ok(dishes);
    }
}
