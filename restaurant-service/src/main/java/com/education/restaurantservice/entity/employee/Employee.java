package com.education.restaurantservice.entity.employee;

import jakarta.persistence.*;
import lombok.*;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    @ToString.Exclude
    private String password;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}