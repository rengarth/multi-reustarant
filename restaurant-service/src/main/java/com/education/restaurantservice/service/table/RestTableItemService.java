package com.education.restaurantservice.service.table;

import com.education.restaurantservice.entity.menu.Dish;
import com.education.restaurantservice.entity.table.RestTableItem;
import com.education.restaurantservice.exception.menu.DishIsDeletedException;
import com.education.restaurantservice.exception.menu.DishIsUnavailableException;
import com.education.restaurantservice.exception.menu.DishNotFoundException;
import com.education.restaurantservice.exception.table.RestTableItemNotFoundException;
import com.education.restaurantservice.repository.menu.DishRepository;
import com.education.restaurantservice.repository.table.RestTableItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestTableItemService {

    private final RestTableItemRepository restTableItemRepository;
    private final DishRepository dishRepository;

    public RestTableItem createRestTableItem(Long dishId, int quantity) {
        log.info("Creating rest table item for dish with id: {} and quantity: {}...", dishId, quantity);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> {
                    log.error("Dish with id: {} not found", dishId);
                    return new DishNotFoundException("Dish with id: " + dishId + " not found");
                });

        if (dish.getIsDeleted()) {
            log.error("Dish with id: {} has been deleted", dishId);
            throw new DishIsDeletedException("Dish has been deleted");
        }
        if (!dish.getIsAvailable()) {
            log.error("Dish with id: {} is unavailable", dishId);
            throw new DishIsUnavailableException("Dish is unavailable");
        }
        if (quantity > dish.getStockQuantity()) {
            log.error("Quantity {} exceeds available stock for dish with id: {}", quantity, dishId);
            throw new IllegalArgumentException("Quantity cannot exceed available stock");
        }

        RestTableItem restTableItem = new RestTableItem();
        restTableItem.setDish(dish);
        restTableItem.setQuantity(quantity);
        restTableItem = restTableItemRepository.save(restTableItem);
        log.info("Rest table item created with id: {}", restTableItem.getId());
        return restTableItem;
    }

    public void updateRestTableItem(Long restTableId, int newQuantity) {
        log.info("Updating rest table item with id: {} to new quantity: {}...", restTableId, newQuantity);

        RestTableItem restTableItem = restTableItemRepository.findById(restTableId)
                .orElseThrow(() -> {
                    log.error("Rest table item with id: {} not found", restTableId);
                    return new RestTableItemNotFoundException("Table item with id: " + restTableId + " not found");
                });

        Dish dish = restTableItem.getDish();

        if (newQuantity < 0) {
            log.error("Quantity cannot be negative: {}", newQuantity);
            throw new IllegalArgumentException("Quantity cannot be negative");
        } else if (newQuantity == 0) {
            deleteRestTableItem(restTableId);
            return;
        } else if (newQuantity > dish.getStockQuantity()) {
            log.error("Quantity {} exceeds available stock for dish with id: {}", newQuantity, dish.getId());
            throw new IllegalArgumentException("Quantity cannot exceed available stock");
        }

        restTableItem.setQuantity(newQuantity);
        restTableItemRepository.save(restTableItem);
        log.info("Rest table item with id: {} updated to new quantity: {}", restTableId, newQuantity);
    }

    public void deleteRestTableItem(Long restTableItemId) {
        log.info("Deleting rest table item with id: {}...", restTableItemId);

        if (!restTableItemRepository.existsById(restTableItemId)) {
            log.error("Rest table item with id: {} not found", restTableItemId);
            throw new RestTableItemNotFoundException("Table item with id: " + restTableItemId + " not found");
        }

        restTableItemRepository.deleteById(restTableItemId);
        log.info("Rest table item with id: {} deleted successfully", restTableItemId);
    }
}

