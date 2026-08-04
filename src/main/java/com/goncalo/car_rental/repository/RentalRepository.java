package com.goncalo.car_rental.repository;

import com.goncalo.car_rental.model.Rental;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {

	List<Rental> findByCarId(Long carId);
}
