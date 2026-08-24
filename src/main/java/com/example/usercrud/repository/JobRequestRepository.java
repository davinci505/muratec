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
    Optional<JobRequest> findByJobNo(String jobNo);

    List<JobRequest> findByJobNoContainingIgnoreCaseOrRequesterContainingIgnoreCase(String jobNo, String requester);

    // Pagination support for Tabulator
    Page<JobRequest> findByJobNoContainingIgnoreCase(String jobNo, Pageable pageable);
    Page<JobRequest> findByRequesterContainingIgnoreCase(String requester, Pageable pageable);
    Page<JobRequest> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);
    Page<JobRequest> findByJobNoContainingIgnoreCaseOrRequesterContainingIgnoreCase(String jobNo, String requester, Pageable pageable);
    Page<JobRequest> findByJobNoContainingIgnoreCaseOrRequesterContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(String jobNo, String requester, String customerName, Pageable pageable);
}
