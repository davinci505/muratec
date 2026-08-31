package com.example.usercrud.controller;

import com.example.usercrud.model.Quote;
import com.example.usercrud.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quotes")
public class QuoteRestController {

    @Autowired
    private QuoteService quoteService;

    @GetMapping
    public Map<String, Object> getQuotes(
            @RequestParam(value = "jobId", required = false) Long jobId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "ccsQuoteNo", required = false) String ccsQuoteNo,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order) {

        // Tabulator uses 1-based page, Spring uses 0-based
        int springPage = Math.max(0, page - 1);
        Pageable pageable = PageRequest.of(springPage, size,
            "asc".equalsIgnoreCase(order) ?
                Sort.by(sort).ascending() :
                Sort.by(sort).descending());

        Page<Quote> quotePage = quoteService.getByFilters(jobId, status, ccsQuoteNo, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("last_page", quotePage.getTotalPages());
        response.put("data", quotePage.getContent().stream().map(this::toMap).collect(Collectors.toList()));

        return response;
    }

    private Map<String, Object> toMap(Quote quote) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", quote.getId());
        map.put("jobRequestId", quote.getJobRequest() != null ? quote.getJobRequest().getId() : null);
        map.put("division", quote.getJobRequest() != null ? quote.getJobRequest().getDivision() : "");
        map.put("jobNo", quote.getJobRequest() != null ? quote.getJobRequest().getJobNo() : "");
        map.put("customerName", quote.getJobRequest() != null ? quote.getJobRequest().getCustomerName() : "");
        map.put("requester", quote.getJobRequest() != null ? quote.getJobRequest().getRequester() : "");
        map.put("requestDate", quote.getJobRequest() != null && quote.getJobRequest().getRequestDate() != null
                ? quote.getJobRequest().getRequestDate().toString() : "");
        map.put("ccsQuoteDate", quote.getCcsQuoteDate() != null ? quote.getCcsQuoteDate().toString() : "");
        map.put("ccsQuoteNo", quote.getCcsQuoteNo());
        map.put("ccsAmount", formatAmount(quote.getCcsAmount()));
        map.put("description", quote.getDescription());
        map.put("brtQuoteNo", quote.getBrtQuoteNo());
        map.put("brtQuoteDate", quote.getBrtQuoteDate() != null ? quote.getBrtQuoteDate().toString() : "");
        map.put("brtAmount", formatAmount(quote.getBrtAmount()));
        map.put("brtNegotiatedAmount", quote.getBrtNegotiatedAmount());
        map.put("status", quote.getStatus());
        return map;
    }

    private String formatAmount(BigDecimal amount) {
        if (amount == null) return "";
        return amount.toPlainString();
    }
}
