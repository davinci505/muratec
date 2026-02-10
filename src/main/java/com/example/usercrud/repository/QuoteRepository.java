package com.example.usercrud.repository;

import com.example.usercrud.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    List<Quote> findByJobRequestId(Long jobRequestId);

    List<Quote> findByJobRequestIdAndStatus(Long jobRequestId, String status);

    List<Quote> findByStatus(String status);

    Optional<Quote> findByCcsQuoteNo(String ccsQuoteNo);

    List<Quote> findByCcsQuoteNoContainingIgnoreCase(String ccsQuoteNo);

    List<Quote> findByJobRequestIdAndCcsQuoteNoContainingIgnoreCase(Long jobRequestId, String ccsQuoteNo);

    List<Quote> findByStatusAndCcsQuoteNoContainingIgnoreCase(String status, String ccsQuoteNo);

    List<Quote> findByJobRequestIdAndStatusAndCcsQuoteNoContainingIgnoreCase(Long jobRequestId, String status, String ccsQuoteNo);
}
