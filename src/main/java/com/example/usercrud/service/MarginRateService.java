package com.example.usercrud.service;

import com.example.usercrud.model.MarginRate;
import com.example.usercrud.repository.MarginRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarginRateService {

    private static final String CATEGORY_EXPENSE = "EXPENSE";
    private static final String CATEGORY_MARGIN_VALUE = "MARGIN_VALUE";

    @Autowired
    private MarginRateRepository marginRateRepository;

    public List<MarginRate> getAllMarginRates() {
        return marginRateRepository.findAll();
    }

    public List<MarginRate> getExpenseRates() {
        return marginRateRepository.findAll().stream()
                .filter(this::isExpenseRate)
                .toList();
    }

    public List<MarginRate> getMarginValues() {
        return marginRateRepository.findAll().stream()
                .filter(this::isMarginValueRate)
                .toList();
    }

    public Optional<MarginRate> getMarginRateById(Long id) {
        return marginRateRepository.findById(id);
    }

    public MarginRate saveMarginRate(MarginRate marginRate) {
        return marginRateRepository.save(marginRate);
    }

    public void deleteMarginRate(Long id) {
        marginRateRepository.deleteById(id);
    }

    public MarginRate updateMarginRate(Long id, MarginRate details) {
        MarginRate marginRate = marginRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MarginRate not found"));

        marginRate.setName(details.getName());
        marginRate.setCategory(CATEGORY_EXPENSE);
        marginRate.setCustomsDutyRate(details.getCustomsDutyRate());
        marginRate.setFreightRate(details.getFreightRate());
        marginRate.setInsuranceRate(details.getInsuranceRate());
        marginRate.setDomesticTransportRate(details.getDomesticTransportRate());
        marginRate.setVatRate(details.getVatRate());
        marginRate.setCustomsClearanceRate(details.getCustomsClearanceRate());
        marginRate.setWarehouseRate(details.getWarehouseRate());

        return marginRateRepository.save(marginRate);
    }

    public MarginRate updateMarginValue(Long id, MarginRate details) {
        MarginRate marginRate = marginRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MarginRate not found"));

        marginRate.setName(details.getName());
        marginRate.setCategory(CATEGORY_MARGIN_VALUE);
        marginRate.setYenExchangeRate(details.getYenExchangeRate());
        marginRate.setMarginRate(details.getMarginRate());

        return marginRateRepository.save(marginRate);
    }

    private boolean hasExpenseFields(MarginRate rate) {
        return rate.getCustomsDutyRate() != null
                || rate.getFreightRate() != null
                || rate.getInsuranceRate() != null
                || rate.getDomesticTransportRate() != null
                || rate.getVatRate() != null
                || rate.getCustomsClearanceRate() != null
                || rate.getWarehouseRate() != null;
    }

    private boolean hasMarginValueFields(MarginRate rate) {
        return rate.getYenExchangeRate() != null
                || rate.getMarginRate() != null;
    }

    private boolean isExpenseRate(MarginRate rate) {
        if (CATEGORY_EXPENSE.equals(rate.getCategory())) {
            return true;
        }
        if (CATEGORY_MARGIN_VALUE.equals(rate.getCategory())) {
            return false;
        }
        return hasExpenseFields(rate);
    }

    private boolean isMarginValueRate(MarginRate rate) {
        if (CATEGORY_MARGIN_VALUE.equals(rate.getCategory())) {
            return true;
        }
        if (CATEGORY_EXPENSE.equals(rate.getCategory())) {
            return false;
        }
        return hasMarginValueFields(rate);
    }
}
