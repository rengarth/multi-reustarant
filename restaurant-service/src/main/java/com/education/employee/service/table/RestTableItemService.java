package com.education.employee.service.table;

import com.education.employee.entity.menu.Dish;
import com.education.employee.entity.table.RestTableItem;
import com.education.employee.exception.menu.DishIsDeletedException;
import com.education.employee.exception.menu.DishIsUnavailableException;
import com.education.employee.exception.menu.DishNotFoundException;
import com.education.employee.exception.table.RestTableItemNotFoundException;
import com.education.employee.repository.menu.DishRepository;
import com.education.employee.repository.table.RestTableItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestTableItemService {

    private final RestTableItemRepository restTableItemRepository;
    private final DishRepository dishRepository;

    public RestTableItem createRestTableItem(Long dishId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new DishNotFoundException("Dish with id: " + dishId + " not found"));

        if (dish.getIsDeleted()) {
            throw new DishIsDeletedException("Dish has been deleted");
        }
        if (!dish.getIsAvailable()) {
            throw new DishIsUnavailableException("Dish is unavailable");
        }
        if (quantity > dish.getStockQuantity()) {
            throw new IllegalArgumentException("Quantity cannot exceed available stock");
        }

        RestTableItem restTableItem = new RestTableItem();
        restTableItem.setDish(dish);
        restTableItem.setQuantity(quantity);
        return restTableItemRepository.save(restTableItem);
    }

    public RestTableItem updateRestTableItem(Long restTableId, int newQuantity) {
        RestTableItem restTableItem = restTableItemRepository.findById(restTableId)
                .orElseThrow(() -> new RestTableItemNotFoundException("Table item with id: " + restTableId + " not found"));

        Dish dish = restTableItem.getDish();

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        } else if (newQuantity == 0) {
            deleteRestTableItem(restTableId);
            return null;
        } else if (newQuantity > dish.getStockQuantity()) {
            throw new IllegalArgumentException("Quantity cannot exceed available stock");
        }

        restTableItem.setQuantity(newQuantity);
        return restTableItemRepository.save(restTableItem);
    }

    public void deleteRestTableItem(Long restTableItemId) {
        if (!restTableItemRepository.existsById(restTableItemId)) {
            throw new RestTableItemNotFoundException("Table item with id: " + restTableItemId + " not found");
        }
        restTableItemRepository.deleteById(restTableItemId);
    }

}
