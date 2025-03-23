package com.education.employee.repository.menu;

import com.education.employee.entity.menu.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
