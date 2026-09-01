package com.example.usercrud.service;

import com.example.usercrud.model.QuotePart;
import com.example.usercrud.repository.QuotePartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class QuotePartService {

    @Autowired
    private QuotePartRepository quotePartRepository;

    @SuppressWarnings("null")
    public List<QuotePart> search(Long quoteId, String q) {
        String query = (q == null || !StringUtils.hasText(q)) ? null : q.trim();
            return quotePartRepository.search(quoteId, query);
        }

    @SuppressWarnings("null")
    public Optional<QuotePart> getQuotePartById(Long id) {
        return quotePartRepository.findById(id);
    }

    public QuotePart saveQuotePart(QuotePart quotePart) {
        return quotePartRepository.save(quotePart);
    }

    public void deleteQuotePart(Long id) {
        quotePartRepository.deleteById(id);
    }

    public void deleteByQuoteId(Long quoteId) {
        quotePartRepository.deleteByQuoteId(quoteId);
    }

    @SuppressWarnings("null")
    public QuotePart updateQuotePart(Long id, QuotePart details) {
        QuotePart quotePart = quotePartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuotePart not found"));

        quotePart.setPartName(details.getPartName());
        quotePart.setPartNumber(details.getPartNumber());
        quotePart.setSpec(details.getSpec());
        quotePart.setQuantity(details.getQuantity());
        quotePart.setSortOrder(details.getSortOrder());

        return quotePartRepository.save(quotePart);
    }
}
