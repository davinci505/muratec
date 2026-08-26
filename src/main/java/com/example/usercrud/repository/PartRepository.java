package com.example.usercrud.repository;

import com.example.usercrud.model.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

    Optional<Part> findByPartNumber(String partNumber);

    List<Part> findByPartNumberContainingIgnoreCase(String partNumber);

    List<Part> findByPartNameContainingIgnoreCase(String partName);

    List<Part> findByPartNumberContainingIgnoreCaseAndPartNameContainingIgnoreCase(String partNumber, String partName);

    // Pagination support
    Page<Part> findByPartNumberContainingIgnoreCase(String partNumber, Pageable pageable);

    Page<Part> findByPartNameContainingIgnoreCase(String partName, Pageable pageable);

    Page<Part> findByPartNumberContainingIgnoreCaseAndPartNameContainingIgnoreCase(String partNumber, String partName, Pageable pageable);
}