package com.example.usercrud.service;

import com.example.usercrud.model.Quote;
import com.example.usercrud.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    public Optional<Quote> getQuoteById(Long id) {
        return quoteRepository.findById(id);
    }

    public Optional<Quote> findByCcsQuoteNo(String ccsQuoteNo) {
        if (ccsQuoteNo == null || !StringUtils.hasText(ccsQuoteNo)) {
            return Optional.empty();
        }
        return quoteRepository.findByCcsQuoteNo(ccsQuoteNo.trim());
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

    public List<Quote> getByFilters(Long jobRequestId, String status, String ccsQuoteNo) {
        boolean hasJob = jobRequestId != null;
        boolean hasStatus = status != null && StringUtils.hasText(status);
        String keyword = (ccsQuoteNo != null && StringUtils.hasText(ccsQuoteNo)) ? ccsQuoteNo.trim() : null;
        boolean hasCcsQuoteNo = keyword != null;

        if (hasJob && hasStatus && hasCcsQuoteNo) {
            return quoteRepository.findByJobRequestIdAndStatusAndCcsQuoteNoContainingIgnoreCase(jobRequestId, status,
                    keyword);
        }
        if (hasJob && hasStatus) {
            return quoteRepository.findByJobRequestIdAndStatus(jobRequestId, status);
        }
        if (hasJob && hasCcsQuoteNo) {
            return quoteRepository.findByJobRequestIdAndCcsQuoteNoContainingIgnoreCase(jobRequestId, keyword);
        }
        if (hasStatus && hasCcsQuoteNo) {
            return quoteRepository.findByStatusAndCcsQuoteNoContainingIgnoreCase(status, keyword);
        }
        if (hasJob) {
            return quoteRepository.findByJobRequestId(jobRequestId);
        }
        if (hasStatus) {
            return quoteRepository.findByStatus(status);
        }
        if (hasCcsQuoteNo) {
            return quoteRepository.findByCcsQuoteNoContainingIgnoreCase(keyword);
        }
        return quoteRepository.findAll();
    }

    public Quote saveQuote(Quote quote) {
        return quoteRepository.save(quote);
    }

    public void deleteQuote(Long id) {
        quoteRepository.deleteById(id);
    }

    public Quote updateQuote(Long id, Quote details) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        quote.setJobRequest(details.getJobRequest());
        quote.setCcsQuoteDate(details.getCcsQuoteDate());
        quote.setCcsQuoteNo(details.getCcsQuoteNo());
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
