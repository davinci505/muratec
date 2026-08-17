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

    public List<QuotePart> search(Long quoteId, String q) {
        String query = (q == null || !StringUtils.hasText(q)) ? null : q.trim();
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
        quotePart.setPartNoProductSpec(details.getPartNoProductSpec());
        quotePart.setCcsPoNo(details.getCcsPoNo());
        quotePart.setWorkNoSerialNo(details.getWorkNoSerialNo());
        quotePart.setOrderQuantity(details.getOrderQuantity());
        quotePart.setCcsPoAmount(details.getCcsPoAmount());
        quotePart.setHmxOrderNo(details.getHmxOrderNo());
        quotePart.setHmxOrderAmount(details.getHmxOrderAmount());
        quotePart.setStatus(details.getStatus());
        quotePart.setDeliveryDate(details.getDeliveryDate());
        quotePart.setRemark(details.getRemark());

        return quotePartRepository.save(quotePart);
    }
}
