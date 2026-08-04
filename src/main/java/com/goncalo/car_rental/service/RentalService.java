package com.goncalo.car_rental.service;

import com.goncalo.car_rental.model.Car;
import com.goncalo.car_rental.model.Customer;
import com.goncalo.car_rental.model.Rental;
import com.goncalo.car_rental.repository.CarRepository;
import com.goncalo.car_rental.repository.CustomerRepository;
import com.goncalo.car_rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;

    public Rental createRental(Long carId, Long customerId, LocalDate startDate, LocalDate endDate) {
        
        // 1. Buscar o Carro pelo ID
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Carro não encontrado"));

        // 2. Verificar se o carro está disponível
        if (!car.isAvailable()) {
            throw new IllegalArgumentException("Carro indisponível");
        }

        // 3. Buscar o Cliente pelo ID
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        // 4. Calcular o número de dias
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) {
            throw new IllegalArgumentException("A data de fim deve ser posterior à data de início");
        }

        // 5. Calcular o preço total
        BigDecimal totalPrice = car.getDailyPrice().multiply(BigDecimal.valueOf(days));

        // 6. Criar e preencher o objeto Rental
        Rental rental = new Rental();
        rental.setCar(car);
        rental.setCustomer(customer);
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setTotalPrice(totalPrice);

        // Guardar o aluguer
        Rental savedRental = rentalRepository.save(rental);

        // 7. Atualizar a disponibilidade do carro
        car.setAvailable(false);
        carRepository.save(car);

        // 8. Retornar o contrato guardado
        return savedRental;
    }
}