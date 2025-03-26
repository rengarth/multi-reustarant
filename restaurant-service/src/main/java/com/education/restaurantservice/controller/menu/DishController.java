package com.education.restaurantservice.controller.menu;

import com.education.kafkadto.dto.menu.DishDTO;
import com.education.restaurantservice.service.menu.DishService;
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
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @Operation(summary = "Get all dishes",
            description = "Retrieve a list of all dishes.")
    @ApiResponse(responseCode = "200",
            description = "Successfully retrieved all dishes",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = DishDTO.class))))
    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        List<DishDTO> dishes = dishService.getAllDishes();
        return ResponseEntity.ok(dishes);
    }

    @Operation(summary = "Get all active dishes",
            description = "Retrieve a list of all active dishes.")
    @ApiResponse(responseCode = "200",
            description = "Successfully retrieved all active dishes",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = DishDTO.class))))
    @GetMapping("/active")
    public ResponseEntity<List<DishDTO>> getAllActiveDishes() {
        List<DishDTO> activeDishes = dishService.getAllActiveDishes();
        return ResponseEntity.ok(activeDishes);
    }

    @Operation(summary = "Get dish by ID",
            description = "Retrieve dish details by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Dish successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Dish not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DishDTO> getDishById(@PathVariable Long id) {
        DishDTO dishDTO = dishService.getDishById(id);
        return ResponseEntity.ok(dishDTO);
    }
}


