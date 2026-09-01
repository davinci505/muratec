package com.example.usercrud.repository;

import com.example.usercrud.model.FormulaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FormulaItemRepository extends JpaRepository<FormulaItem, Long> {

    @Modifying
    @Query("DELETE FROM FormulaItem i WHERE i.formula.id = :formulaId")
    int deleteByFormulaId(@Param("formulaId") Long formulaId);
}
