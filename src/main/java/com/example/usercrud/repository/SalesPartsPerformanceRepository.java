package com.example.usercrud.repository;

import com.example.usercrud.model.SalesPartsPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesPartsPerformanceRepository extends JpaRepository<SalesPartsPerformance, Long> {
    
    List<SalesPartsPerformance> findByCustomerContainingIgnoreCase(String customer);
    
    List<SalesPartsPerformance> findByFactoryNameContainingIgnoreCase(String factoryName);
    
    List<SalesPartsPerformance> findByQuoteNoContainingIgnoreCase(String quoteNo);
    
    List<SalesPartsPerformance> findByPoNoContainingIgnoreCase(String poNo);
    
    List<SalesPartsPerformance> findByCustomerContainingIgnoreCaseAndFactoryNameContainingIgnoreCase(String customer, String factoryName);
    
    List<SalesPartsPerformance> findByCustomerContainingIgnoreCaseAndQuoteNoContainingIgnoreCase(String customer, String quoteNo);
    
    List<SalesPartsPerformance> findByFactoryNameContainingIgnoreCaseAndQuoteNoContainingIgnoreCase(String factoryName, String quoteNo);
}