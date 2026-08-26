package com.example.usercrud.repository;

import com.example.usercrud.model.JobRequestPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRequestPartRepository extends JpaRepository<JobRequestPart, Long> {
    List<JobRequestPart> findByJobRequestId(Long jobRequestId);
}