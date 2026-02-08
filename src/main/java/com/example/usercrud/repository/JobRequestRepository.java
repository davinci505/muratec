package com.example.usercrud.repository;

import com.example.usercrud.model.JobRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {
    Optional<JobRequest> findByJobNo(String jobNo);
}
