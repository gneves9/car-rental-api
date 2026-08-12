package com.goncalo.car_rental.controller;

import com.goncalo.car_rental.model.Rental;
import com.goncalo.car_rental.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        try {
            return rentalService.createRental(carId, customerId, startDate, endDate);
        } catch (IllegalArgumentException ex) {
            HttpStatus status = (ex.getMessage() != null && ex.getMessage().contains("não encontrado"))
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;
            throw new org.springframework.web.server.ResponseStatusException(status, ex.getMessage(), ex);
        }
    }
}