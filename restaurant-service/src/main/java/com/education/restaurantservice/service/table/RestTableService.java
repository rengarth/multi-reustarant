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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestTableService {

    private final RestTableRepository restTableRepository;
    private final RestTableItemService restTableItemService;
    private final DishRepository dishRepository;
    private final WaiterService waiterService;

    public void setTableToCurrentWaiter(Integer number) {
        Waiter waiter = waiterService.getCurrentWaiter();
        RestTable table = restTableRepository.findByNumberAndWaiterIsNull(number)
                .orElseThrow(() -> new RestTableIsOccupiedException(
                        "Table with number: "
                                + number + " is occupied"));
        table.setWaiter(waiter);
        restTableRepository.save(table);
    }

    public RestTableDTO calculateTable(Integer number, RequestRestTableDTO tableDTO) {
        int quantity = tableDTO.getQuantity();
        long dishId = tableDTO.getDishId();
        RestTable table = getTableOfCurrentWaiter(number);

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new DishNotFoundException("Dish with id: " + dishId + " not found"));
        if (dish.getIsDeleted()) {
            throw new DishIsDeletedException("Dish with id: " + dishId + " is deleted");
        } else if (!dish.getIsAvailable()) {
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
                throw new IllegalArgumentException("Quantity of dish with id: "
                        + dishId
                        + ", being added to the cart exceeds its stock availability");
            }
            if (existingItemQuantitySum <= 0) {
                table.removeItem(existingItem.get());
                restTableItemService.deleteRestTableItem(existingItem.get().getId());
            } else {
                restTableItemService.updateRestTableItem(existingItem.get().getId(), existingItemQuantitySum);
            }
        } else {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }
            if (quantity > dish.getStockQuantity()) {
                throw new IllegalArgumentException("Quantity of product with id: "
                        + dishId
                        + ", being added to the cart exceeds its stock availability");
            }
            RestTableItem restTableItem = restTableItemService.createRestTableItem(dish.getId(), quantity);
            table.addItem(restTableItem);
        }
        table.setTotalAmount(table.calculateTotalAmount());
        restTableRepository.save(table);
        return RestTableUtils.convertRestTableToRestTableDTO(table);
    }

    public RestTableDTO getTable(Integer number) {
        RestTable table = getTableOfCurrentWaiter(number);
        return RestTableUtils.convertRestTableToRestTableDTO(table);
    }

    public RestTable getTableOfCurrentWaiter(Integer number) {
        RestTable table = restTableRepository.findByNumber(number)
                .orElseThrow(() -> new RestTableNotFoundException("Table with number: " + number + " not found"));
        if (table.getWaiter() == null) {
            throw new RestTableIsNotAssignedException("Table with number: " + number + " is not assigned to any waiter");
        } else if (!table.getWaiter().equals(waiterService.getCurrentWaiter())) {
            throw new InsufficientPrivilegiesException("This waiter is not assigned to this table");
        }
        return table;
    }

    public void clearTable(Integer number) {
        RestTable table = getTableOfCurrentWaiter(number);
        table.getTableItems().clear();
        table.setTotalAmount(0);
        restTableRepository.save(table);
    }
}
