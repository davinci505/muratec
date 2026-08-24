package com.example.usercrud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "parts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_number", length = 50, nullable = false, unique = true)
    private String partNumber;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "spec", length = 500)
    private String spec;

    @Column(name = "price_jpy", precision = 15, scale = 2)
    private BigDecimal priceJpy;
}