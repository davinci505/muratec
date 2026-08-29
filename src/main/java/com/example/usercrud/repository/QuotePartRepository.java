package com.example.usercrud.repository;

import com.example.usercrud.model.QuotePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface QuotePartRepository extends JpaRepository<QuotePart, Long> {
    @Query("select qp from QuotePart qp where (:quoteId is null or qp.quote.id = :quoteId) and " +
            "(:q is null or lower(qp.partName) like lower(concat('%', :q, '%')) " +
            "or lower(qp.partNumber) like lower(concat('%', :q, '%')) " +
                "or lower(qp.spec) like lower(concat('%', :q, '%')))")
        List<QuotePart> search(@Param("quoteId") Long quoteId, @Param("q") String q);

    @Modifying
    @Transactional
    @Query("delete from QuotePart qp where qp.quote.id = :quoteId")
    void deleteByQuoteId(@Param("quoteId") Long quoteId);
}
