package com.example.usercrud.repository;

import com.example.usercrud.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

    Optional<Part> findByPartNumber(String partNumber);

    List<Part> findByPartNumberContainingIgnoreCase(String partNumber);

    List<Part> findByDescriptionContainingIgnoreCase(String description);

    List<Part> findByPartNumberContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String partNumber, String description);
}