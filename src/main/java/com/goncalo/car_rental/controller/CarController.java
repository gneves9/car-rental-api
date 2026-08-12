package com.goncalo.car_rental.controller;

import com.goncalo.car_rental.model.Car;
import com.goncalo.car_rental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Diz ao Spring que esta classe responde a pedidos da Web
@RequestMapping("/api/cars") // O URL base para todos os métodos desta classe
@RequiredArgsConstructor
public class CarController {

    private final CarRepository carRepository;

    // Responde a pedidos GET em http://localhost:8080/api/cars
    @GetMapping
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // Responde a pedidos POST em http://localhost:8080/api/cars
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Devolve o código HTTP 201 (Created) em vez de 200 (OK)
    public Car createCar(@RequestBody Car car) {
        // @RequestBody converte o JSON que o cliente enviou num objeto Java "Car"
        return carRepository.save(car);
    }
}