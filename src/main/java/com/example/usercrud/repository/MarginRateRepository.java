package com.example.usercrud.repository;

import com.example.usercrud.model.MarginRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarginRateRepository extends JpaRepository<MarginRate, Long> {
}
