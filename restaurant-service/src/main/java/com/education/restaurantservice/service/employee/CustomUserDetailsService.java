package com.education.restaurantservice.service.employee;

import com.education.restaurantservice.entity.employee.Admin;
import com.education.restaurantservice.entity.employee.Waiter;
import com.education.restaurantservice.repository.employee.AdminRepository;
import com.education.restaurantservice.repository.employee.WaiterRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final WaiterRepository waiterRepository;

    public CustomUserDetailsService(AdminRepository adminRepository, WaiterRepository waiterRepository) {
        this.adminRepository = adminRepository;
        this.waiterRepository = waiterRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByPhoneNumber(phoneNumber)
                .orElse(null);

        if (admin != null && !admin.isDeleted()) {
            return new org.springframework.security.core.userdetails.User(
                    admin.getPhoneNumber(),
                    admin.getPassword(),
                    List.of(new SimpleGrantedAuthority("ADMIN"))
            );
        }

        Waiter waiter = waiterRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User with phone number " + phoneNumber + " not found"));

        return new org.springframework.security.core.userdetails.User(
                waiter.getPhoneNumber(),
                waiter.getPassword(),
                List.of(new SimpleGrantedAuthority("WAITER"))
        );
    }
}
