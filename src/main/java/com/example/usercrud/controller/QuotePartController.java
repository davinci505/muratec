package com.example.usercrud.controller;

import com.example.usercrud.model.Quote;
import com.example.usercrud.model.QuotePart;
import com.example.usercrud.service.MarginRateService;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quote-parts")
public class QuotePartController {

    @Autowired
    private QuotePartService quotePartService;

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private MarginRateService marginRateService;

    @GetMapping
    public String listQuoteParts(@RequestParam(value = "quoteId", required = false) Long quoteId,
                                 @RequestParam(value = "q", required = false) String q,
                                 Model model) {
        List<QuotePart> quoteParts = quotePartService.search(quoteId, q);
        model.addAttribute("quoteParts", quoteParts);
        model.addAttribute("quoteId", quoteId);
        model.addAttribute("q", q);
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
        model.addAttribute("marginRates", marginRateService.getAllMarginRates());
        return "quote-parts/form";
    }

    @GetMapping("/bulk")
    public String showBulkCreateForm() {
        return "quote-parts/bulk-form";
    }

    @PostMapping
    public String createQuotePart(@ModelAttribute QuotePart quotePart,
                                  @RequestParam("quoteId") Long quoteId,
                                  @RequestParam(value = "marginRateId", required = false) Long marginRateId) {
        Quote quote = quoteService.getQuoteById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));
        quotePart.setQuote(quote);
        if (marginRateId != null) {
            marginRateService.getMarginRateById(marginRateId).ifPresent(quotePart::setMarginRate);
        } else {
            quotePart.setMarginRate(null);
        }
        quotePartService.saveQuotePart(quotePart);
        return "redirect:/quote-parts";
    }

    @PostMapping("/bulk")
    @ResponseBody
    public String createBulkQuoteParts(@RequestBody List<QuotePartBulkRequest> quoteParts) {
        int saved = 0;
        for (QuotePartBulkRequest req : quoteParts) {
            Optional<Quote> quote = quoteService.findByCcsQuoteNo(req.ccsQuoteNo);
            if (quote.isEmpty()) {
                continue;
            }
            QuotePart quotePart = new QuotePart();
            quotePart.setQuote(quote.get());
            quotePart.setFactoryName(req.factoryName);
            quotePart.setProductName(req.productName);
            quotePart.setProductSpec(req.productSpec);
            quotePart.setPartNo(req.partNo);
            quotePart.setNewPartsNo(req.newPartsNo);
            quotePart.setModel(req.model);
            quotePart.setMachineName(req.machineName);
            quotePart.setType(req.type);
            quotePart.setUnitName(req.unitName);
            quotePart.setDescription(req.description);
            quotePart.setMaker(req.maker);
            quotePart.setMurataPartsNo(req.murataPartsNo);
            quotePart.setPartQuantity(req.partQuantity);
            quotePart.setQuoteQuantity(req.quoteQuantity);
            quotePart.setUnitPriceBrt(req.unitPriceBrt);
            quotePart.setUnitPriceHmx(req.unitPriceHmx);
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
        model.addAttribute("marginRates", marginRateService.getAllMarginRates());
        return "quote-parts/form";
    }

    @PostMapping("/{id}")
    public String updateQuotePart(@PathVariable Long id,
                                  @ModelAttribute QuotePart quotePart,
                                  @RequestParam("quoteId") Long quoteId,
                                  @RequestParam(value = "marginRateId", required = false) Long marginRateId) {
        Quote quote = quoteService.getQuoteById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));
        quotePart.setQuote(quote);
        if (marginRateId != null) {
            marginRateService.getMarginRateById(marginRateId).ifPresent(quotePart::setMarginRate);
        } else {
            quotePart.setMarginRate(null);
        }
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
        public String factoryName;
        public String productName;
        public String productSpec;
        public String partNo;
        public String newPartsNo;
        public String model;
        public String machineName;
        public String type;
        public String unitName;
        public String description;
        public String maker;
        public String murataPartsNo;
        public Integer partQuantity;
        public Integer quoteQuantity;
        public BigDecimal unitPriceBrt;
        public BigDecimal unitPriceHmx;
        public String remark;
    }
}
