package com.education.restaurantservice.entity.table;

import com.education.restaurantservice.entity.employee.Waiter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tables")
@Getter
@Setter
public class RestTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer number;

    @ManyToOne
    @JoinColumn(name = "waiter_id")
    private Waiter waiter;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "table_items_list",
            joinColumns = @JoinColumn(name = "table_id"),
            inverseJoinColumns = @JoinColumn(name = "table_item_id"))
    private List<RestTableItem> tableItems = new ArrayList<>();

    @Column(name = "total_amount")
    private int totalAmount;

    public Integer calculateTotalAmount() {
        return tableItems.stream().mapToInt(RestTableItem::getTotalPrice).sum();
    }

    public void addItem(RestTableItem item) {
        tableItems.add(item);
    }

    public void removeItem(RestTableItem item) {
        tableItems.remove(item);
    }
}

