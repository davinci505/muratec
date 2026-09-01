package com.example.usercrud.service;

import com.example.usercrud.model.SalesPartsPerformance;
import com.example.usercrud.repository.SalesPartsPerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class SalesPartsPerformanceService {

    @Autowired
    private SalesPartsPerformanceRepository repository;

    public List<SalesPartsPerformance> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Optional<SalesPartsPerformance> getById(Long id) {
        return repository.findById(id);
    }

    public List<SalesPartsPerformance> getByFilters(String customer, String factoryName, String quoteNo, String poNo) {
        boolean hasCustomer = customer != null && StringUtils.hasText(customer);
        boolean hasFactory = factoryName != null && StringUtils.hasText(factoryName);
        boolean hasQuoteNo = quoteNo != null && StringUtils.hasText(quoteNo);
        boolean hasPoNo = poNo != null && StringUtils.hasText(poNo);

        String c = hasCustomer ? customer.trim() : null;
        String f = hasFactory ? factoryName.trim() : null;
        String q = hasQuoteNo ? quoteNo.trim() : null;
        String p = hasPoNo ? poNo.trim() : null;

        if (hasCustomer && hasFactory && hasQuoteNo && hasPoNo) {
            // Fallback to customer + factory + quoteNo combination
            return repository.findByCustomerContainingIgnoreCaseAndFactoryNameContainingIgnoreCase(c, f);
        }
        if (hasCustomer && hasFactory) {
            return repository.findByCustomerContainingIgnoreCaseAndFactoryNameContainingIgnoreCase(c, f);
        }
        if (hasCustomer && hasQuoteNo) {
            return repository.findByCustomerContainingIgnoreCaseAndQuoteNoContainingIgnoreCase(c, q);
        }
        if (hasFactory && hasQuoteNo) {
            return repository.findByFactoryNameContainingIgnoreCaseAndQuoteNoContainingIgnoreCase(f, q);
        }
        if (hasCustomer) {
            return repository.findByCustomerContainingIgnoreCase(c);
        }
        if (hasFactory) {
            return repository.findByFactoryNameContainingIgnoreCase(f);
        }
        if (hasQuoteNo) {
            return repository.findByQuoteNoContainingIgnoreCase(q);
        }
        if (hasPoNo) {
            return repository.findByPoNoContainingIgnoreCase(p);
        }
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public SalesPartsPerformance save(SalesPartsPerformance entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    @SuppressWarnings("null")
    public SalesPartsPerformance update(Long id, SalesPartsPerformance details) {
        SalesPartsPerformance entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SalesPartsPerformance not found with id: " + id));

        entity.setCustomer(details.getCustomer());
        entity.setFactoryName(details.getFactoryName());
        entity.setContent(details.getContent());
        entity.setQuoteNo(details.getQuoteNo());
        entity.setQuoteAmount(details.getQuoteAmount());
        entity.setOrderDateOrderNo(details.getOrderDateOrderNo());
        entity.setOrderAmountExclVat(details.getOrderAmountExclVat());
        entity.setDeliveryDate(details.getDeliveryDate());
        entity.setInvoiceIssueDate(details.getInvoiceIssueDate());
        entity.setPaymentDate(details.getPaymentDate());
        entity.setPoNo(details.getPoNo());
        entity.setOrderAmount(details.getOrderAmount());
        entity.setOrderAmountJpyToKrw(details.getOrderAmountJpyToKrw());
        entity.setWarehouseReceiptDate(details.getWarehouseReceiptDate());
        entity.setInvoice(details.getInvoice());
        entity.setInvoiceDate(details.getInvoiceDate());
        entity.setDays6090(details.getDays6090());
        entity.setMurataRemittanceDate(details.getMurataRemittanceDate());
        entity.setShippingCompany(details.getShippingCompany());
        entity.setCustomsDuty(details.getCustomsDuty());
        entity.setVat(details.getVat());
        entity.setFreightCustoms(details.getFreightCustoms());
        entity.setTotalImportCost(details.getTotalImportCost());
        entity.setNetProfit(details.getNetProfit());

        return repository.save(entity);
    }
}