package com.goncalo.car_rental.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Diz ao Spring que isto é uma entidade de base de dados
@Table(name = "cars") 
@Data // Lombok: Gera getters, setters, toString, etc. automaticamente
@NoArgsConstructor // Lombok: Gera um construtor vazio (exigido pelo JPA)
@AllArgsConstructor // Lombok: Gera um construtor com todos os atributos
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false, unique = true)
    private String licensePlate;

    @Column(nullable = false)
    private BigDecimal dailyPrice;

    private boolean available = true;
}
