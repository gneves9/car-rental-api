package com.goncalo.car_rental.service;

import com.goncalo.car_rental.model.Car;
import com.goncalo.car_rental.model.Customer;
import com.goncalo.car_rental.model.Rental;
import com.goncalo.car_rental.repository.CarRepository;
import com.goncalo.car_rental.repository.CustomerRepository;
import com.goncalo.car_rental.repository.RentalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) 
class RentalServiceTest {

    @Mock // Cria uma versão falsa do repositório
    private CarRepository carRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks // Injeta os repositórios falsos no nosso Service real
    private RentalService rentalService;

    @Test
    void deveCriarAluguerComSucessoECalcularPrecoCorreto() {
        // 1. ARRANGE (Preparar o cenário e os dados falsos)
        Car car = new Car(1L, "Toyota", "Corolla", "AA-11-AA", BigDecimal.valueOf(50), true);
        Customer customer = new Customer(1L, "João", "Silva", "joao@email.com", "123456", "912345678");
        
        // Ensinar o Mockito o que responder quando o Service chamar a base de dados
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3); // Aluguer de 3 dias

        // 2. ACT (Executar a ação que queremos testar)
        Rental rental = rentalService.createRental(1L, 1L, startDate, endDate);

        // 3. ASSERT (Verificar se o resultado foi o esperado)
        assertNotNull(rental);
        // Preço diário 50 * 3 dias = 150
        assertEquals(BigDecimal.valueOf(150), rental.getTotalPrice()); 
        assertFalse(car.isAvailable()); // Verifica se o estado do carro mudou
        verify(rentalRepository, times(1)).save(any(Rental.class)); // Garante que foi guardado na BD
    }

    @Test
    void deveLancarExcecaoSeCarroNaoEstiverDisponivel() {
        // 1. ARRANGE (O carro falso agora tem available = false)
        Car car = new Car(1L, "Toyota", "Corolla", "AA-11-AA", BigDecimal.valueOf(50), false); 
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);

        // 2 & 3. ACT & ASSERT (Testar se a exceção é lançada corretamente)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rentalService.createRental(1L, 1L, startDate, endDate);
        });

        assertEquals("Carro indisponível", exception.getMessage());
        verify(rentalRepository, never()).save(any(Rental.class)); // Garante que nunca guardou o erro na BD
    }
}