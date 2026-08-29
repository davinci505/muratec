package com.example.usercrud.controller;

import com.example.usercrud.model.JobRequest;
import com.example.usercrud.model.JobRequestPart;
import com.example.usercrud.model.Quote;
import com.example.usercrud.model.QuotePart;
import com.example.usercrud.service.JobRequestService;
import com.example.usercrud.service.QuotePartService;
import com.example.usercrud.service.QuoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quotes")
public class QuoteController {

    private static final List<String> STATUS_OPTIONS = Arrays.asList(
            "견적중",
            "재견적",
            "견적제출",
            "발주예정"
    );

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private JobRequestService jobRequestService;

    @Autowired
    private QuotePartService quotePartService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public String listQuotes(@RequestParam(value = "jobId", required = false) Long jobId,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "ccsQuoteNo", required = false) String ccsQuoteNo,
                             Model model) {
        List<Quote> quotes = quoteService.getByFilters(jobId, status, ccsQuoteNo);

        if (jobId != null) {
            JobRequest jobRequest = jobRequestService.getJobRequestById(jobId).orElse(null);
            model.addAttribute("jobRequest", jobRequest);
        }

        model.addAttribute("quotes", quotes);
        model.addAttribute("statusOptions", STATUS_OPTIONS);
        model.addAttribute("status", status);
        model.addAttribute("jobId", jobId);
        model.addAttribute("ccsQuoteNo", ccsQuoteNo);
        return "quotes/list";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(value = "jobId", required = false) Long jobId,
                                 Model model) {
        Quote quote = new Quote();
        if (jobId != null) {
            jobRequestService.getJobRequestById(jobId).ifPresent(quote::setJobRequest);
        }
        model.addAttribute("quote", quote);
        model.addAttribute("jobs", jobRequestService.getAllJobRequests());
        model.addAttribute("statusOptions", STATUS_OPTIONS);
        model.addAttribute("pageTitle", "새 견적 추가 - BARATEC");
        return "quotes/form";
    }

    @GetMapping("/bulk")
    public String showBulkCreateForm(Model model) {
        model.addAttribute("statusOptions", STATUS_OPTIONS);
        return "quotes/bulk-form";
    }

    @PostMapping
    public String createQuote(@ModelAttribute Quote quote,
                              BindingResult bindingResult,
                              @RequestParam("jobRequestId") Long jobRequestId,
                              @RequestParam(value = "partsJson", required = false) String partsJson,
                              Model model) {
        if (isStatusMissing(quote)) {
            if (jobRequestId != null) {
                jobRequestService.getJobRequestById(jobRequestId).ifPresent(quote::setJobRequest);
            }
            model.addAttribute("jobs", jobRequestService.getAllJobRequests());
            model.addAttribute("statusOptions", STATUS_OPTIONS);
            model.addAttribute("statusError", true);
            model.addAttribute("errorMessage", "상태를 선택하세요.");
            return "quotes/form";
        }
        if (bindingResult.hasErrors()) {
            if (jobRequestId != null) {
                jobRequestService.getJobRequestById(jobRequestId).ifPresent(quote::setJobRequest);
            }
            model.addAttribute("jobs", jobRequestService.getAllJobRequests());
            model.addAttribute("statusOptions", STATUS_OPTIONS);
            model.addAttribute("errorMessage", "날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 입력하세요.");
            return "quotes/form";
        }
        JobRequest jobRequest = jobRequestService.getJobRequestById(jobRequestId)
                .orElseThrow(() -> new RuntimeException("JobRequest not found"));
        quote.setJobRequest(jobRequest);
        Quote savedQuote = quoteService.saveQuote(quote);
        
        // Save QuoteParts from partsJson
        if (partsJson != null && !partsJson.isEmpty()) {
            saveQuoteParts(savedQuote, jobRequest, partsJson);
        }
        
        return "redirect:/quotes";
    }

    @PostMapping("/bulk")
    @ResponseBody
    public String createBulkQuotes(@RequestBody List<QuoteBulkRequest> quotes) {
        int saved = 0;
        for (QuoteBulkRequest req : quotes) {
            Optional<JobRequest> jobRequest = jobRequestService.findByJobNo(req.jobNo);
            if (!jobRequest.isPresent()) {
                continue;
            }
            Quote quote = new Quote();
            quote.setJobRequest(jobRequest.get());
            quote.setCcsQuoteDate(req.ccsQuoteDate);
            quote.setCcsQuoteNo(req.ccsQuoteNo);
            quote.setCcsAmount(req.ccsAmount);
            quote.setDescription(req.description);
            quote.setBrtQuoteDate(req.brtQuoteDate);
            quote.setBrtQuoteNo(req.brtQuoteNo);
            quote.setBrtAmount(req.brtAmount);
            quote.setBrtNegotiatedAmount(req.brtNegotiatedAmount);
            quote.setStatus(req.status);
            quoteService.saveQuote(quote);
            saved++;
        }
        return "success:" + saved;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Quote quote = quoteService.getQuoteById(id)
                .orElseThrow(() -> new RuntimeException("Quote not found"));
        
        // Load QuoteParts for this quote
        List<QuotePart> quoteParts = quotePartService.search(id, null);
        model.addAttribute("quoteParts", quoteParts);
        
        model.addAttribute("quote", quote);
        model.addAttribute("jobs", jobRequestService.getAllJobRequests());
        model.addAttribute("statusOptions", STATUS_OPTIONS);
        return "quotes/form";   
    }

    @PostMapping("/{id}")
    public String updateQuote(@PathVariable Long id,
                              @ModelAttribute Quote quote,
                              BindingResult bindingResult,
                              @RequestParam("jobRequestId") Long jobRequestId,
                              @RequestParam(value = "partsJson", required = false) String partsJson,
                              Model model) {
        if (isStatusMissing(quote)) {
            if (jobRequestId != null) {
                jobRequestService.getJobRequestById(jobRequestId).ifPresent(quote::setJobRequest);
            }
            model.addAttribute("jobs", jobRequestService.getAllJobRequests());
            model.addAttribute("statusOptions", STATUS_OPTIONS);
            model.addAttribute("statusError", true);
            model.addAttribute("errorMessage", "상태를 선택하세요.");
            return "quotes/form";
        }
        if (bindingResult.hasErrors()) {
            if (jobRequestId != null) {
                jobRequestService.getJobRequestById(jobRequestId).ifPresent(quote::setJobRequest);
            }
            model.addAttribute("jobs", jobRequestService.getAllJobRequests());
            model.addAttribute("statusOptions", STATUS_OPTIONS);
            model.addAttribute("errorMessage", "날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 입력하세요.");
            return "quotes/form";
        }
        JobRequest jobRequest = jobRequestService.getJobRequestById(jobRequestId)
                .orElseThrow(() -> new RuntimeException("JobRequest not found"));
        quote.setJobRequest(jobRequest);
        quoteService.updateQuote(id, quote);
        
        // Save QuoteParts from partsJson (replace existing)
        if (partsJson != null && !partsJson.isEmpty()) {
            saveQuoteParts(quote, jobRequest, partsJson);
        } else {
            // If no partsJson, delete all existing QuoteParts for this quote
            quotePartService.deleteByQuoteId(id);
        }
        
        return "redirect:/quotes";
    }

    @GetMapping("/delete/{id}")
    public String deleteQuote(@PathVariable Long id) {
        quoteService.deleteQuote(id);
        return "redirect:/quotes";
    }

    private boolean isStatusMissing(Quote quote) {
        return quote == null || quote.getStatus() == null || !StringUtils.hasText(quote.getStatus());
    }

    @GetMapping("/api/job-request-parts")
    @ResponseBody
    public List<JobRequestPart> getJobRequestParts(@RequestParam("jobRequestId") Long jobRequestId) {
        return jobRequestService.getPartsByJobRequestId(jobRequestId);
    }

    /**
     * Save QuoteParts from partsJson data.
     * Deletes existing QuoteParts for the quote and creates new ones from the JSON data.
     */
    private void saveQuoteParts(Quote quote, JobRequest jobRequest, String partsJson) {
        try {
            // Parse partsJson as list of maps (from frontend Tabulator)
            List<Map<String, Object>> partsData = objectMapper.readValue(partsJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
            
            // Delete existing QuoteParts for this quote
            quotePartService.deleteByQuoteId(quote.getId());
            
            // Create QuotePart for each part
            for (int i = 0; i < partsData.size(); i++) {
                Map<String, Object> partData = partsData.get(i);
                QuotePart quotePart = new QuotePart();
                quotePart.setQuote(quote);
                quotePart.setPartName((String) partData.get("partName"));
                quotePart.setPartNumber((String) partData.get("partNumber"));
                quotePart.setSpec((String) partData.get("spec"));
                
                Object qtyObj = partData.get("quantity");
                if (qtyObj instanceof Number) {
                    quotePart.setQuantity(((Number) qtyObj).intValue());
                } else if (qtyObj instanceof String) {
                    try {
                        quotePart.setQuantity(Integer.parseInt((String) qtyObj));
                    } catch (NumberFormatException e) {
                        quotePart.setQuantity(1);
                    }
                } else {
                    quotePart.setQuantity(1);
                }
                
                Object sortObj = partData.get("sortOrder");
                if (sortObj instanceof Number) {
                    quotePart.setSortOrder(((Number) sortObj).intValue());
                } else if (sortObj instanceof String) {
                    try {
                        quotePart.setSortOrder(Integer.parseInt((String) sortObj));
                    } catch (NumberFormatException e) {
                        quotePart.setSortOrder(i);
                    }
                } else {
                    quotePart.setSortOrder(i);
                }
                
                // Try to find and link Part entity by partNumber
                String partNumber = quotePart.getPartNumber();
                if (partNumber != null && !partNumber.isEmpty()) {
                    // We need PartRepository - for now skip linking
                    // Part partEntity = partRepository.findByPartNumber(partNumber).orElse(null);
                    // if (partEntity != null) {
                    //     quotePart.setPart(partEntity);
                    // }
                }
                
                quotePartService.saveQuotePart(quotePart);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error parsing parts JSON: " + e.getMessage(), e);
        }
    }

    public static class QuoteBulkRequest {
        public String jobNo;
        public LocalDate ccsQuoteDate;
        public String ccsQuoteNo;
        public BigDecimal ccsAmount;
        public String description;
        public LocalDate brtQuoteDate;
        public String brtQuoteNo;
        public BigDecimal brtAmount;
        public String brtNegotiatedAmount;
        public String status;
    }
}
