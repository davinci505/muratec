package com.example.usercrud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "margin_rates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarginRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", length = 30)
    private String category;

    @Column(name = "yen_exchange_rate", precision = 15, scale = 4)
    private BigDecimal yenExchangeRate;

    @Column(name = "margin_rate", precision = 7, scale = 2)
    private BigDecimal marginRate;

    @Column(name = "customs_duty_rate", precision = 7, scale = 2)
    private BigDecimal customsDutyRate;

    @Column(name = "freight_rate", precision = 7, scale = 2)
    private BigDecimal freightRate;

    @Column(name = "insurance_rate", precision = 7, scale = 2)
    private BigDecimal insuranceRate;

    @Column(name = "domestic_transport_rate", precision = 7, scale = 2)
    private BigDecimal domesticTransportRate;

    @Column(name = "vat_rate", precision = 7, scale = 2)
    private BigDecimal vatRate;

    @Column(name = "customs_clearance_rate", precision = 7, scale = 2)
    private BigDecimal customsClearanceRate;

    @Column(name = "warehouse_rate", precision = 7, scale = 2)
    private BigDecimal warehouseRate;

    @Transient
    public BigDecimal getTotalRate() {
        return sum(
                customsDutyRate,
                freightRate,
                insuranceRate,
                domesticTransportRate,
                vatRate,
                customsClearanceRate,
                warehouseRate,
                marginRate
        );
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
}
