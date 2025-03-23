package com.education.employee.repository.table;

import com.education.employee.entity.table.RestTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestTableRepository extends JpaRepository<RestTable, Long> {
    Optional<RestTable> findByIdAndWaiterIsNull(Long id);
}
