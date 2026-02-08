package com.example.usercrud.repository;

import com.example.usercrud.model.QuotePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotePartRepository extends JpaRepository<QuotePart, Long> {
    @Query("select qp from QuotePart qp where (:quoteId is null or qp.quote.id = :quoteId) and " +
            "(:q is null or lower(qp.partNo) like lower(concat('%', :q, '%')) " +
            "or lower(qp.productName) like lower(concat('%', :q, '%')))")
    List<QuotePart> search(@Param("quoteId") Long quoteId, @Param("q") String q);
}
