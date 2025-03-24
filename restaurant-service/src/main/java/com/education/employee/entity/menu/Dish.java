package com.education.employee.entity.menu;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dishes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price_per_one")
    private Integer pricePerOne;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
