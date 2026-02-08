package com.example.usercrud.service;

import com.example.usercrud.model.MarginRate;
import com.example.usercrud.repository.MarginRateRepository;
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
        marginRate.setCustomsDutyRate(details.getCustomsDutyRate());
        marginRate.setFreightRate(details.getFreightRate());
        marginRate.setInsuranceRate(details.getInsuranceRate());
        marginRate.setDomesticTransportRate(details.getDomesticTransportRate());
        marginRate.setVatRate(details.getVatRate());
        marginRate.setCustomsClearanceRate(details.getCustomsClearanceRate());
        marginRate.setWarehouseRate(details.getWarehouseRate());

        return marginRateRepository.save(marginRate);
    }
}
