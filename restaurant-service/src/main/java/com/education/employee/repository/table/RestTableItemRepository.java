package com.education.employee.repository.table;

import com.education.employee.entity.table.RestTableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestTableItemRepository extends JpaRepository<RestTableItem, Long> {
}
