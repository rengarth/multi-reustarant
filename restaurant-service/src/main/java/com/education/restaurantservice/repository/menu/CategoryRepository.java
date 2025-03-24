package com.education.restaurantservice.repository.menu;

import com.education.restaurantservice.entity.menu.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
