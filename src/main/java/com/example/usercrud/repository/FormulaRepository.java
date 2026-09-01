package com.example.usercrud.repository;

import com.example.usercrud.model.Formula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormulaRepository extends JpaRepository<Formula, Long> {

    Optional<Formula> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT f FROM Formula f LEFT JOIN FETCH f.items ORDER BY f.code ASC, f.id ASC")
    List<Formula> findAllWithItems();

    List<Formula> findAllByOrderByCodeAsc();

    @Query("SELECT f FROM Formula f LEFT JOIN FETCH f.items WHERE f.code = :code")
    Optional<Formula> findByCodeWithItems(String code);
}
