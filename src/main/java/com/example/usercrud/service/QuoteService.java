package com.example.usercrud.service;

import com.example.usercrud.model.Quote;
import com.example.usercrud.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    public Optional<Quote> getQuoteById(Long id) {
        return quoteRepository.findById(id);
    }

    public Optional<Quote> findByJobRequestNo(String jobRequestNo) {
        if (jobRequestNo == null || !StringUtils.hasText(jobRequestNo)) {
            return Optional.empty();
        }
        return quoteRepository.findByJobRequestNo(jobRequestNo.trim());
    }

    public List<Quote> getByJobRequest(Long jobRequestId) {
        return quoteRepository.findByJobRequestId(jobRequestId);
    }

    public List<Quote> getByStatus(String status) {
        return quoteRepository.findByStatus(status);
    }

    public List<Quote> getByJobRequestAndStatus(Long jobRequestId, String status) {
        return quoteRepository.findByJobRequestIdAndStatus(jobRequestId, status);
    }

    public List<Quote> getByFilters(Long jobRequestId, String status, String jobRequestNo) {
        boolean hasJob = jobRequestId != null;
        boolean hasStatus = status != null && StringUtils.hasText(status);
        String keyword = (jobRequestNo != null && StringUtils.hasText(jobRequestNo)) ? jobRequestNo.trim() : null;
        boolean hasJobRequestNo = keyword != null;

        if (hasJob && hasStatus && hasJobRequestNo) {
            return quoteRepository.findByJobRequestIdAndStatusAndJobRequestNoContainingIgnoreCase(jobRequestId, status,
                    keyword);
        }
        if (hasJob && hasStatus) {
            return quoteRepository.findByJobRequestIdAndStatus(jobRequestId, status);
        }
        if (hasJob && hasJobRequestNo) {
            return quoteRepository.findByJobRequestIdAndJobRequestNoContainingIgnoreCase(jobRequestId, keyword);
        }
        if (hasStatus && hasJobRequestNo) {
            return quoteRepository.findByStatusAndJobRequestNoContainingIgnoreCase(status, keyword);
        }
        if (hasJob) {
            return quoteRepository.findByJobRequestId(jobRequestId);
        }
        if (hasStatus) {
            return quoteRepository.findByStatus(status);
        }
        if (hasJobRequestNo) {
            return quoteRepository.findByJobRequestNoContainingIgnoreCase(keyword);
        }
        return quoteRepository.findAll();
    }

    public Quote saveQuote(Quote quote) {
        return quoteRepository.save(quote);
    }

    /**
     * Pageable variant of getByFilters for Tabulator remote pagination.
     * Mirrors the same conditional logic as the non-paged version.
     */
    public Page<Quote> getByFilters(Long jobRequestId, String status, String jobRequestNo, Pageable pageable) {
        boolean hasJob = jobRequestId != null;
        boolean hasStatus = status != null && StringUtils.hasText(status);
        String keyword = (jobRequestNo != null && StringUtils.hasText(jobRequestNo)) ? jobRequestNo.trim() : null;
        boolean hasJobRequestNo = keyword != null;

        if (hasJob && hasStatus && hasJobRequestNo) {
            return quoteRepository.findByJobRequestIdAndStatusAndJobRequestNoContainingIgnoreCase(
                    jobRequestId, status, keyword, pageable);
        }
        if (hasJob && hasStatus) {
            return quoteRepository.findByJobRequestIdAndStatus(jobRequestId, status, pageable);
        }
        if (hasJob && hasJobRequestNo) {
            return quoteRepository.findByJobRequestIdAndJobRequestNoContainingIgnoreCase(
                    jobRequestId, keyword, pageable);
        }
        if (hasStatus && hasJobRequestNo) {
            return quoteRepository.findByStatusAndJobRequestNoContainingIgnoreCase(status, keyword, pageable);
        }
        if (hasJob) {
            return quoteRepository.findByJobRequestId(jobRequestId, pageable);
        }
        if (hasStatus) {
            return quoteRepository.findByStatus(status, pageable);
        }
        if (hasJobRequestNo) {
            return quoteRepository.findByJobRequestNoContainingIgnoreCase(keyword, pageable);
        }
        return quoteRepository.findAll(pageable);
    }

    public void deleteQuote(Long id) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quote not found: " + id));
        quoteRepository.delete(quote);
    }

    public Quote updateQuote(Long id, Quote details) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        quote.setJobRequest(details.getJobRequest());
        quote.setJobrequestDate(details.getJobrequestDate());
        quote.setJobRequestNo(details.getJobRequestNo());
        quote.setCcsAmount(details.getCcsAmount());
        quote.setDescription(details.getDescription());
        quote.setBrtQuoteNo(details.getBrtQuoteNo());
        quote.setBrtQuoteDate(details.getBrtQuoteDate());
        quote.setBrtAmount(details.getBrtAmount());
        quote.setBrtNegotiatedAmount(details.getBrtNegotiatedAmount());
        quote.setStatus(details.getStatus());

        return quoteRepository.save(quote);
    }
}
