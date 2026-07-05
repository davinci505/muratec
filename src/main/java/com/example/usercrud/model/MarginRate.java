package com.example.usercrud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Entity
@Table(name = "margin_rates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarginRate {

    private interface UnitPriceCalculator {
        BigDecimal calculate(MarginRate rate, BigDecimal unitPrice);
    }

    private static final UnitPriceCalculator HMX_CALCULATOR = (rate, unitPrice) -> {
        if (rate.marginRate == null || BigDecimal.ZERO.compareTo(rate.marginRate) == 0) {
            return null;
        }
        BigDecimal numerator = unitPrice.multiply(rate.yenExchangeRate);
        return numerator.divide(rate.marginRate, 2, RoundingMode.HALF_UP);
    };

    private static final UnitPriceCalculator BRT_CALCULATOR = (rate, unitPrice) -> {
        if (rate.transportClearanceRate == null) {
            return null;
        }
        return unitPrice.multiply(rate.transportClearanceRate)
                .multiply(rate.yenExchangeRate)
                .setScale(2, RoundingMode.HALF_UP);
    };

    private static final UnitPriceCalculator DEFAULT_CALCULATOR = (rate, unitPrice) -> {
        if (rate.marginRate == null || BigDecimal.ZERO.compareTo(rate.marginRate) == 0) {
            return null;
        }
        BigDecimal numerator = unitPrice.multiply(rate.yenExchangeRate);
        return numerator.divide(rate.marginRate, 2, RoundingMode.HALF_UP);
    };

    private static final Map<String, UnitPriceCalculator> CALCULATORS_BY_NAME = Map.of(
            "BRT", BRT_CALCULATOR,
            "HMX", HMX_CALCULATOR
    );

    @Id
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "yen_exchange_rate", precision = 15, scale = 4)
    private BigDecimal yenExchangeRate;

    @Column(name = "margin_rate", precision = 7, scale = 2)
    @ColumnDefault("0.65")
    private BigDecimal marginRate;

    @Column(name = "transport_clearance_rate", precision = 7, scale = 2)
    private BigDecimal transportClearanceRate;

    @Transient
    public BigDecimal getTotalRate() {
        return transportClearanceRate == null ? BigDecimal.ZERO : transportClearanceRate;
    }

    @Transient
    public BigDecimal getTransportClearanceRate() {
        return transportClearanceRate;
    }

    @PrePersist
    public void applyDefaults() {
        if (marginRate == null) {
            marginRate = new BigDecimal("0.65");
        }
    }

    private BigDecimal sum(BigDecimal... values) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            if (value != null) {
                total = total.add(value);
            }
        }
        return total;
    }

    @Transient
    public boolean isHmxType() {
        return name != null && "HMX".equalsIgnoreCase(name);
    }

    @Transient
    public BigDecimal getDisplayRateValue() {
        if (isHmxType()) {
            return marginRate;
        }
        return transportClearanceRate != null ? transportClearanceRate : marginRate;
    }

    @Transient
    public BigDecimal calculateUnitPriceWithMargin(BigDecimal unitPrice) {
        if (unitPrice == null) {
            return null;
        }
        BigDecimal exchangeRate = yenExchangeRate;
        if (exchangeRate == null || BigDecimal.ZERO.compareTo(exchangeRate) == 0) {
            return null;
        }

        String typeName = name == null ? "" : name.toUpperCase();
        UnitPriceCalculator calculator = CALCULATORS_BY_NAME.getOrDefault(typeName, DEFAULT_CALCULATOR);
        return calculator.calculate(this, unitPrice);
    }
}
