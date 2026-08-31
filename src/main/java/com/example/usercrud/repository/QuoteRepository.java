package com.example.usercrud.repository;

import com.example.usercrud.model.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    List<Quote> findByJobRequestId(Long jobRequestId);

    List<Quote> findByJobRequestIdAndStatus(Long jobRequestId, String status);

    List<Quote> findByStatus(String status);

    Optional<Quote> findByJobRequestNo(String jobRequestNo);

    List<Quote> findByJobRequestNoContainingIgnoreCase(String jobRequestNo);

    List<Quote> findByJobRequestIdAndJobRequestNoContainingIgnoreCase(Long jobRequestId, String jobRequestNo);

    List<Quote> findByStatusAndJobRequestNoContainingIgnoreCase(String status, String jobRequestNo);

    List<Quote> findByJobRequestIdAndStatusAndJobRequestNoContainingIgnoreCase(Long jobRequestId, String status, String jobRequestNo);

    // Pageable variants for Tabulator remote pagination
    Page<Quote> findByJobRequestId(Long jobRequestId, Pageable pageable);

    Page<Quote> findByJobRequestIdAndStatus(Long jobRequestId, String status, Pageable pageable);

    Page<Quote> findByStatus(String status, Pageable pageable);

    Page<Quote> findByJobRequestNoContainingIgnoreCase(String jobRequestNo, Pageable pageable);

    Page<Quote> findByJobRequestIdAndJobRequestNoContainingIgnoreCase(Long jobRequestId, String jobRequestNo, Pageable pageable);

    Page<Quote> findByStatusAndJobRequestNoContainingIgnoreCase(String status, String jobRequestNo, Pageable pageable);

    Page<Quote> findByJobRequestIdAndStatusAndJobRequestNoContainingIgnoreCase(Long jobRequestId, String status, String jobRequestNo, Pageable pageable);
}
