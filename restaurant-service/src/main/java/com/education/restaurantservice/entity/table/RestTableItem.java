package com.education.restaurantservice.entity.table;

import com.education.restaurantservice.entity.menu.Dish;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "table_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestTableItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(nullable = false)
    private Integer quantity;

    public Integer getTotalPrice() {
        return dish.getPricePerOne() * quantity;
    }
}
