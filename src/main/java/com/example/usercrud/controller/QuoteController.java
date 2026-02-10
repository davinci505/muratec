package com.example.usercrud.controller;

import com.example.usercrud.model.JobRequest;
import com.example.usercrud.model.Quote;
import com.example.usercrud.service.JobRequestService;
import com.example.usercrud.service.QuoteService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quotes")
public class QuoteController {

    private static final List<String> STATUS_OPTIONS = List.of(
            "견적중",
            "재견적",
            "발주전",
            "납품예정",
            "납품완료"
    );

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private JobRequestService jobRequestService;

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
        quoteService.saveQuote(quote);
        return "redirect:/quotes";
    }

    @PostMapping("/bulk")
    @ResponseBody
    public String createBulkQuotes(@RequestBody List<QuoteBulkRequest> quotes) {
        int saved = 0;
        for (QuoteBulkRequest req : quotes) {
            Optional<JobRequest> jobRequest = jobRequestService.findByJobNo(req.jobNo);
            if (jobRequest.isEmpty()) {
                continue;
            }
            Quote quote = new Quote();
            quote.setJobRequest(jobRequest.get());
            quote.setCcsQuoteDate(req.ccsQuoteDate);
            quote.setCcsQuoteNo(req.ccsQuoteNo);
            quote.setCcsAmountBrt(req.ccsAmountBrt);
            quote.setCcsAmountHmx(req.ccsAmountHmx);
            quote.setDescription(req.description);
            quote.setBrtQuoteNo(req.brtQuoteNo);
            quote.setBrtQuoteDate(req.brtQuoteDate);
            quote.setBrtNegotiatedAmount(req.brtNegotiatedAmount);
            quote.setStatus(req.status);
            quote.setStatusDate(req.statusDate);
            quote.setHmxOrderNo(req.hmxOrderNo);
            quote.setHmxOrderDate(req.hmxOrderDate);
            quote.setCcsPoNo(req.ccsPoNo);
            quote.setCcsPoDate(req.ccsPoDate);
            quoteService.saveQuote(quote);
            saved++;
        }
        return "success:" + saved;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Quote quote = quoteService.getQuoteById(id)
                .orElseThrow(() -> new RuntimeException("Quote not found"));
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
        return "redirect:/quotes";
    }

    @GetMapping("/delete/{id}")
    public String deleteQuote(@PathVariable Long id) {
        quoteService.deleteQuote(id);
        return "redirect:/quotes";
    }

    private boolean isStatusMissing(Quote quote) {
        return quote == null || quote.getStatus() == null || quote.getStatus().isBlank();
    }

    public static class QuoteBulkRequest {
        public String jobNo;
        public LocalDate ccsQuoteDate;
        public String ccsQuoteNo;
        public BigDecimal ccsAmountBrt;
        public BigDecimal ccsAmountHmx;
        public String description;
        public String brtQuoteNo;
        public LocalDate brtQuoteDate;
        public BigDecimal brtNegotiatedAmount;
        public String status;
        public LocalDate statusDate;
        public String hmxOrderNo;
        public LocalDate hmxOrderDate;
        public String ccsPoNo;
        public LocalDate ccsPoDate;
    }
}
