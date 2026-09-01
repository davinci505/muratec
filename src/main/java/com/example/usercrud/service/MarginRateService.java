package com.example.usercrud.service;

import com.example.usercrud.model.MarginRate;
import com.example.usercrud.repository.MarginRateRepository;
import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarginRateService {

    @Autowired
    private MarginRateRepository marginRateRepository;

    public List<MarginRate> getAllMarginRates() {
        return marginRateRepository.findAll();
    }

    public List<MarginRate> getExpenseRates() {
        return getAllMarginRates();
    }

    public List<MarginRate> getMarginValues() {
        return getAllMarginRates();
    }

    @SuppressWarnings("null")
    public Optional<MarginRate> getMarginRateById(String id) {
        return marginRateRepository.findById(id);
    }

    public MarginRate saveMarginRate(@NonNull MarginRate marginRate) {
        return marginRateRepository.save(marginRate);
    }

    public void deleteMarginRate(@NonNull String id) {
        marginRateRepository.deleteById(id);
    }

    @SuppressWarnings("null")
    public MarginRate updateMarginRate(String id, MarginRate details) {
        MarginRate marginRate = marginRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MarginRate not found"));

        marginRate.setName(details.getName());
        marginRate.setYenExchangeRate(details.getYenExchangeRate());
        marginRate.setTransportClearanceRate(details.getTransportClearanceRate());
        marginRate.setMarginRate(details.getMarginRate());

        return marginRateRepository.save(marginRate);
    }

    public MarginRate updateMarginValue(String id, MarginRate details) {
        return updateMarginRate(id, details);
    }
}
