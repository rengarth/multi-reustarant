package com.education.restaurantservice.service.table;

import com.education.restaurantservice.dto.table.RequestRestTableDTO;
import com.education.restaurantservice.dto.table.RestTableDTO;
import com.education.restaurantservice.entity.employee.Waiter;
import com.education.restaurantservice.entity.menu.Dish;
import com.education.restaurantservice.entity.table.RestTable;
import com.education.restaurantservice.entity.table.RestTableItem;
import com.education.restaurantservice.exception.employee.InsufficientPrivilegiesException;
import com.education.restaurantservice.exception.menu.DishIsDeletedException;
import com.education.restaurantservice.exception.menu.DishIsUnavailableException;
import com.education.restaurantservice.exception.menu.DishNotFoundException;
import com.education.restaurantservice.exception.table.RestTableIsNotAssignedException;
import com.education.restaurantservice.exception.table.RestTableIsOccupiedException;
import com.education.restaurantservice.exception.table.RestTableNotFoundException;
import com.education.restaurantservice.repository.menu.DishRepository;
import com.education.restaurantservice.repository.table.RestTableRepository;
import com.education.restaurantservice.service.employee.WaiterService;
import com.education.restaurantservice.util.RestTableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestTableService {

    private final RestTableRepository restTableRepository;
    private final RestTableItemService restTableItemService;
    private final DishRepository dishRepository;
    private final WaiterService waiterService;

    public void setTableToCurrentWaiter(Integer number) {
        log.info("Assigning table with number: {} to the current waiter...", number);

        Waiter waiter = waiterService.getCurrentWaiter();
        RestTable table = restTableRepository.findByNumberAndWaiterIsNull(number)
                .orElseThrow(() -> {
                    log.error("Table with number: {} is already occupied", number);
                    return new RestTableIsOccupiedException(
                            "Table with number: " + number + " is occupied");
                });
        table.setWaiter(waiter);
        restTableRepository.save(table);
        log.info("Table with number: {} successfully assigned to waiter with id: {}", number, waiter.getId());
    }

    public RestTableDTO calculateTable(Integer number, RequestRestTableDTO tableDTO) {
        log.info("Calculating table with number: {} and dish id: {} and quantity: {}...", number, tableDTO.getDishId(), tableDTO.getQuantity());

        int quantity = tableDTO.getQuantity();
        long dishId = tableDTO.getDishId();
        RestTable table = getTableOfCurrentWaiter(number);

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> {
                    log.error("Dish with id: {} not found", dishId);
                    return new DishNotFoundException("Dish with id: " + dishId + " not found");
                });
        if (dish.getIsDeleted()) {
            log.error("Dish with id: {} has been deleted", dishId);
            throw new DishIsDeletedException("Dish with id: " + dishId + " is deleted");
        } else if (!dish.getIsAvailable()) {
            log.error("Dish with id: {} is unavailable", dishId);
            throw new DishIsUnavailableException("Dish with id: " + dishId + " is unavailable");
        }

        Optional<RestTableItem> existingItem =
                table.getTableItems()
                        .stream()
                        .filter(item -> item.getDish().equals(dish))
                        .findFirst();

        if (existingItem.isPresent()) {
            int existingItemQuantitySum = existingItem.get().getQuantity() + quantity;
            if (existingItemQuantitySum > dish.getStockQuantity()) {
                log.error("Quantity {} for dish with id: {} exceeds available stock", existingItemQuantitySum, dishId);
                throw new IllegalArgumentException("Quantity of dish with id: "
                        + dishId
                        + ", being added to the cart exceeds its stock availability");
            }
            if (existingItemQuantitySum <= 0) {
                table.removeItem(existingItem.get());
                restTableItemService.deleteRestTableItem(existingItem.get().getId());
                log.info("Dish with id: {} removed from the table", dishId);
            } else {
                restTableItemService.updateRestTableItem(existingItem.get().getId(), existingItemQuantitySum);
                log.info("Dish with id: {} updated on the table with new quantity: {}", dishId, existingItemQuantitySum);
            }
        } else {
            if (quantity <= 0) {
                log.error("Quantity must be greater than 0 for dish with id: {}", dishId);
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }
            if (quantity > dish.getStockQuantity()) {
                log.error("Quantity {} for dish with id: {} exceeds available stock", quantity, dishId);
                throw new IllegalArgumentException("Quantity of product with id: "
                        + dishId
                        + ", being added to the cart exceeds its stock availability");
            }
            RestTableItem restTableItem = restTableItemService.createRestTableItem(dish.getId(), quantity);
            table.addItem(restTableItem);
            log.info("Dish with id: {} added to the table with quantity: {}", dishId, quantity);
        }
        table.setTotalAmount(table.calculateTotalAmount());
        restTableRepository.save(table);
        log.info("Total amount for table with number: {} recalculated and saved", number);
        return RestTableUtils.convertRestTableToRestTableDTO(table);
    }

    public RestTableDTO getTable(Integer number) {
        log.info("Fetching table with number: {} for current waiter...", number);
        RestTable table = getTableOfCurrentWaiter(number);
        return RestTableUtils.convertRestTableToRestTableDTO(table);
    }

    public RestTable getTableOfCurrentWaiter(Integer number) {
        log.info("Fetching table with number: {} assigned to the current waiter...", number);
        RestTable table = restTableRepository.findByNumber(number)
                .orElseThrow(() -> {
                    log.error("Table with number: {} not found", number);
                    return new RestTableNotFoundException("Table with number: " + number + " not found");
                });
        if (table.getWaiter() == null) {
            log.error("Table with number: {} is not assigned to any waiter", number);
            throw new RestTableIsNotAssignedException("Table with number: " + number + " is not assigned to any waiter");
        } else if (!table.getWaiter().equals(waiterService.getCurrentWaiter())) {
            log.error("This waiter is not assigned to table with number: {}", number);
            throw new InsufficientPrivilegiesException("This waiter is not assigned to this table");
        }
        return table;
    }

    public void clearTable(Integer number) {
        log.info("Clearing table with number: {} for current waiter...", number);
        RestTable table = getTableOfCurrentWaiter(number);
        table.getTableItems().clear();
        table.setTotalAmount(0);
        restTableRepository.save(table);
        log.info("Table with number: {} cleared successfully", number);
    }
}

