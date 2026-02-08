package com.example.usercrud.service;

import com.example.usercrud.model.QuotePart;
import com.example.usercrud.repository.QuotePartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuotePartService {

    @Autowired
    private QuotePartRepository quotePartRepository;

    public List<QuotePart> search(Long quoteId, String q) {
        String query = (q == null || q.isBlank()) ? null : q.trim();
        return quotePartRepository.search(quoteId, query);
    }

    public Optional<QuotePart> getQuotePartById(Long id) {
        return quotePartRepository.findById(id);
    }

    public QuotePart saveQuotePart(QuotePart quotePart) {
        return quotePartRepository.save(quotePart);
    }

    public void deleteQuotePart(Long id) {
        quotePartRepository.deleteById(id);
    }

    public QuotePart updateQuotePart(Long id, QuotePart details) {
        QuotePart quotePart = quotePartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuotePart not found"));

        quotePart.setQuote(details.getQuote());
        quotePart.setFactoryName(details.getFactoryName());
        quotePart.setProductName(details.getProductName());
        quotePart.setProductSpec(details.getProductSpec());
        quotePart.setPartNo(details.getPartNo());
        quotePart.setNewPartsNo(details.getNewPartsNo());
        quotePart.setModel(details.getModel());
        quotePart.setMachineName(details.getMachineName());
        quotePart.setType(details.getType());
        quotePart.setUnitName(details.getUnitName());
        quotePart.setDescription(details.getDescription());
        quotePart.setMaker(details.getMaker());
        quotePart.setMurataPartsNo(details.getMurataPartsNo());
        quotePart.setPartQuantity(details.getPartQuantity());
        quotePart.setQuoteQuantity(details.getQuoteQuantity());
        quotePart.setUnitPriceBrt(details.getUnitPriceBrt());
        quotePart.setUnitPriceHmx(details.getUnitPriceHmx());
        quotePart.setRemark(details.getRemark());

        return quotePartRepository.save(quotePart);
    }
}
