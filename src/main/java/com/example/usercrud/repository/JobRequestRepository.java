package com.example.usercrud.repository;

import com.example.usercrud.model.JobRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {
    Optional<JobRequest> findByRequestNo(String requestNo);

    List<JobRequest> findByRequestNoContainingIgnoreCaseOrRequesterContainingIgnoreCase(String requestNo, String requester);

    // Pagination support for Tabulator
    Page<JobRequest> findByRequestNoContainingIgnoreCase(String requestNo, Pageable pageable);
    Page<JobRequest> findByRequesterContainingIgnoreCase(String requester, Pageable pageable);
    Page<JobRequest> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);
    Page<JobRequest> findByRequestNoContainingIgnoreCaseOrRequesterContainingIgnoreCase(String requestNo, String requester, Pageable pageable);
    Page<JobRequest> findByRequestNoContainingIgnoreCaseOrRequesterContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(String requestNo, String requester, String customerName, Pageable pageable);
}
