package com.goncalo.car_rental.controller;

import com.goncalo.car_rental.model.Rental;
import com.goncalo.car_rental.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    // Aqui injetamos o Service em vez do Repository, porque precisamos das validações!
    private final RentalService rentalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rental createRental(@RequestParam Long carId, 
                               @RequestParam Long customerId, 
                               @RequestParam LocalDate startDate, 
                               @RequestParam LocalDate endDate) {
        
        // Passa os dados recebidos pelo URL diretamente para a nossa regra de negócio
        return rentalService.createRental(carId, customerId, startDate, endDate);
    }
}