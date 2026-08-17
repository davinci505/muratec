package com.example.usercrud.controller;

import com.example.usercrud.model.Quote;
import com.example.usercrud.model.QuotePart;
import com.example.usercrud.service.QuotePartService;
import com.example.usercrud.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quote-parts")
public class QuotePartController {

    @Autowired
    private QuotePartService quotePartService;

    @Autowired
    private QuoteService quoteService;

    private static final List<String> STATUS_OPTIONS = Arrays.asList(
                "발주중",
                "납품예정",
                "납품완료"
        );

        @GetMapping
        public String listQuoteParts(@RequestParam(value = "quoteId", required = false) Long quoteId,
                                     @RequestParam(value = "q", required = false) String q,
                                     @RequestParam(value = "status", required = false) String status,
                                     Model model) {
            List<QuotePart> quoteParts = quotePartService.search(quoteId, q, status);
            model.addAttribute("quoteParts", quoteParts);
            model.addAttribute("quoteId", quoteId);
            model.addAttribute("q", q);
            model.addAttribute("status", status);
            model.addAttribute("statusOptions", STATUS_OPTIONS);
            if (quoteId != null) {
                model.addAttribute("quote", quoteService.getQuoteById(quoteId).orElse(null));
            }
            return "quote-parts/list";
        }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(value = "quoteId", required = false) Long quoteId,
                                 Model model) {
        QuotePart quotePart = new QuotePart();
        if (quoteId != null) {
            quoteService.getQuoteById(quoteId).ifPresent(quotePart::setQuote);
        }
        model.addAttribute("quotePart", quotePart);
        model.addAttribute("quotes", quoteService.getAllQuotes());
        return "quote-parts/form";
    }

    @GetMapping("/bulk")
    public String showBulkCreateForm() {
        return "quote-parts/bulk-form";
    }

    @PostMapping
    public String createQuotePart(@ModelAttribute QuotePart quotePart,
                                  @RequestParam("quoteId") Long quoteId) {
        Quote quote = quoteService.getQuoteById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));
        quotePart.setQuote(quote);
        quotePart.setFactoryName(quote.getJobRequest() != null ? quote.getJobRequest().getFactoryName() : null);
        quotePartService.saveQuotePart(quotePart);
        return "redirect:/quote-parts";
    }

    @PostMapping("/bulk")
    @ResponseBody
    public String createBulkQuoteParts(@RequestBody List<QuotePartBulkRequest> quoteParts) {
        int saved = 0;
        for (QuotePartBulkRequest req : quoteParts) {
            Optional<Quote> quote = quoteService.findByCcsQuoteNo(req.ccsQuoteNo);
            if (!quote.isPresent()) {
                continue;
            }
            QuotePart quotePart = new QuotePart();
            quotePart.setQuote(quote.get());
            quotePart.setFactoryName(quote.get().getJobRequest() != null ? quote.get().getJobRequest().getFactoryName() : null);
            quotePart.setProductName(req.productName);
            quotePart.setPartNoProductSpec(req.partNoProductSpec);
            quotePart.setCcsPoNo(req.ccsPoNo);
            quotePart.setWorkNoSerialNo(req.workNoSerialNo);
            quotePart.setOrderQuantity(req.orderQuantity);
            quotePart.setCcsPoAmount(req.ccsPoAmount);
            quotePart.setHmxOrderNo(req.hmxOrderNo);
            quotePart.setHmxOrderAmount(req.hmxOrderAmount);
            quotePart.setStatus(req.status);
            quotePart.setDeliveryDate(req.deliveryDate);
            quotePart.setRemark(req.remark);
            quotePartService.saveQuotePart(quotePart);
            saved++;
        }
        return "success:" + saved;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        QuotePart quotePart = quotePartService.getQuotePartById(id)
                .orElseThrow(() -> new RuntimeException("QuotePart not found"));
        model.addAttribute("quotePart", quotePart);
        model.addAttribute("quotes", quoteService.getAllQuotes());
        return "quote-parts/form";
    }

    @PostMapping("/{id}")
    public String updateQuotePart(@PathVariable Long id,
                                  @ModelAttribute QuotePart quotePart,
                                  @RequestParam("quoteId") Long quoteId) {
        Quote quote = quoteService.getQuoteById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));
        quotePart.setQuote(quote);
        quotePart.setFactoryName(quote.getJobRequest() != null ? quote.getJobRequest().getFactoryName() : null);
        quotePartService.updateQuotePart(id, quotePart);
        return "redirect:/quote-parts";
    }

    @GetMapping("/delete/{id}")
    public String deleteQuotePart(@PathVariable Long id) {
        quotePartService.deleteQuotePart(id);
        return "redirect:/quote-parts";
    }

    public static class QuotePartBulkRequest {
        public String ccsQuoteNo;
        public String productName;
        public String partNoProductSpec;
        public String ccsPoNo;
        public String workNoSerialNo;
        public Integer orderQuantity;
        public BigDecimal ccsPoAmount;
        public String hmxOrderNo;
        public BigDecimal hmxOrderAmount;
        public String status;
        public String deliveryDate;
        public String remark;
    }
}
