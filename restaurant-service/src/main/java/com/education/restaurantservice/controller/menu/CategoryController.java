package com.education.restaurantservice.controller.menu;

import com.education.kafkadto.dto.menu.CategoryDTO;
import com.education.kafkadto.dto.menu.DishDTO;
import com.education.restaurantservice.service.menu.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all categories",
            description = "Retrieve a list of all categories.")
    @ApiResponse(responseCode = "200",
            description = "Successfully retrieved all categories",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class))))
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get category by ID",
            description = "Retrieve category details by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Category successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @Operation(summary = "Get all dishes in a category",
            description = "Retrieve a list of dishes in the specified category.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved dishes in the category",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DishDTO.class)))),
            @ApiResponse(responseCode = "404",
                    description = "Category not found")
    })
    @GetMapping("/{id}/dishes")
    public ResponseEntity<List<DishDTO>> getCategoryDishes(@PathVariable Long id) {
        List<DishDTO> dishes = categoryService.getCategoryDishes(id);
        return ResponseEntity.ok(dishes);
    }
}

