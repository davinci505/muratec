package com.example.usercrud.controller;

import com.example.usercrud.model.SalesPartsPerformance;
import com.example.usercrud.service.SalesPartsPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

@Controller
@RequestMapping("/sales-parts")
public class SalesPartsPerformanceController {

    @Autowired
    private SalesPartsPerformanceService service;

    @GetMapping
    public String list(@RequestParam(value = "customer", required = false) String customer,
                       @RequestParam(value = "factoryName", required = false) String factoryName,
                       @RequestParam(value = "quoteNo", required = false) String quoteNo,
                       @RequestParam(value = "poNo", required = false) String poNo,
                       Model model) {
        List<SalesPartsPerformance> list = service.getByFilters(customer, factoryName, quoteNo, poNo);
        model.addAttribute("list", list);
        model.addAttribute("customer", customer);
        model.addAttribute("factoryName", factoryName);
        model.addAttribute("quoteNo", quoteNo);
        model.addAttribute("poNo", poNo);
        return "sales-parts/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("entity", new SalesPartsPerformance());
        model.addAttribute("pageTitle", "새 판매부품 실적 추가 - BARATEC");
        return "sales-parts/form";
    }

    @GetMapping("/bulk")
    public String showBulkForm(Model model) {
        return "sales-parts/bulk-form";
    }

    @PostMapping
    public String create(@ModelAttribute SalesPartsPerformance entity,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "입력 값이 올바르지 않습니다. 날짜는 YYYY-MM-DD 형식으로 입력하세요.");
            return "sales-parts/form";
        }
        service.save(entity);
        return "redirect:/sales-parts";
    }

    @PostMapping("/bulk")
    @ResponseBody
    public String createBulk(@RequestBody List<BulkRequest> requests) {
        int saved = 0;
        for (BulkRequest req : requests) {
            SalesPartsPerformance entity = new SalesPartsPerformance();
            entity.setCustomer(req.customer);
            entity.setFactoryName(req.factoryName);
            entity.setContent(req.content);
            entity.setQuoteNo(req.quoteNo);
            entity.setQuoteAmount(req.quoteAmount);
            entity.setOrderDateOrderNo(req.orderDateOrderNo);
            entity.setOrderAmountExclVat(req.orderAmountExclVat);
            entity.setDeliveryDate(req.deliveryDate);
            entity.setInvoiceIssueDate(req.invoiceIssueDate);
            entity.setPaymentDate(req.paymentDate);
            entity.setPoNo(req.poNo);
            entity.setOrderAmount(req.orderAmount);
            entity.setOrderAmountJpyToKrw(req.orderAmountJpyToKrw);
            entity.setWarehouseReceiptDate(req.warehouseReceiptDate);
            entity.setInvoice(req.invoice);
            entity.setInvoiceDate(req.invoiceDate);
            entity.setDays6090(req.days6090);
            entity.setMurataRemittanceDate(req.murataRemittanceDate);
            entity.setShippingCompany(req.shippingCompany);
            entity.setCustomsDuty(req.customsDuty);
            entity.setVat(req.vat);
            entity.setFreightCustoms(req.freightCustoms);
            entity.setTotalImportCost(req.totalImportCost);
            entity.setNetProfit(req.netProfit);
            service.save(entity);
            saved++;
        }
        return "success:" + saved;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        SalesPartsPerformance entity = service.getById(id)
                .orElseThrow(() -> new RuntimeException("SalesPartsPerformance not found"));
        model.addAttribute("entity", entity);
        model.addAttribute("pageTitle", "판매부품 실적 수정 - BARATEC");
        return "sales-parts/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute SalesPartsPerformance entity,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "입력 값이 올바르지 않습니다. 날짜는 YYYY-MM-DD 형식으로 입력하세요.");
            return "sales-parts/form";
        }
        service.update(id, entity);
        return "redirect:/sales-parts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/sales-parts";
    }

    public static class BulkRequest {
        public String customer;
        public String factoryName;
        public String content;
        public String quoteNo;
        public BigDecimal quoteAmount;
        public String orderDateOrderNo;
        public BigDecimal orderAmountExclVat;
        public LocalDate deliveryDate;
        public LocalDate invoiceIssueDate;
        public LocalDate paymentDate;
        public String poNo;
        public BigDecimal orderAmount;
        public BigDecimal orderAmountJpyToKrw;
        public LocalDate warehouseReceiptDate;
        public String invoice;
        public LocalDate invoiceDate;
        public String days6090;
        public LocalDate murataRemittanceDate;
        public String shippingCompany;
        public BigDecimal customsDuty;
        public BigDecimal vat;
        public BigDecimal freightCustoms;
        public BigDecimal totalImportCost;
        public BigDecimal netProfit;
    }
}