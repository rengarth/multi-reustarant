package com.education.restaurantservice.entity.table;

import com.education.restaurantservice.entity.menu.Dish;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "table_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestTableItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private RestTable table;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(nullable = false)
    private Integer quantity;

    public Integer getTotalPrice() {
        return dish.getPricePerOne() * quantity;
    }
}
