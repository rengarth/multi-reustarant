package com.education.restaurantservice.repository.table;

import com.education.restaurantservice.entity.table.RestTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestTableRepository extends JpaRepository<RestTable, Long> {
    Optional<RestTable> findByNumberAndWaiterIsNull(Integer number);
    Optional<RestTable> findByNumber(Integer number);
}
