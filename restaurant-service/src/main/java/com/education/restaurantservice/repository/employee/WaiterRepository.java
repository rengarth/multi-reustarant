package com.education.restaurantservice.repository.employee;

import com.education.restaurantservice.entity.employee.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaiterRepository extends JpaRepository<Waiter, Long> {
    Optional<Waiter> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}
