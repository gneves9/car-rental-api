package com.goncalo.car_rental.repository;

import com.goncalo.car_rental.model.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
} 

