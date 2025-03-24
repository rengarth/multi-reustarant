package com.education.restaurantservice.entity.table;

import com.education.restaurantservice.entity.employee.Waiter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer number;

    @ManyToOne
    @JoinColumn(name = "waiter_id")
    private Waiter waiter;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
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

