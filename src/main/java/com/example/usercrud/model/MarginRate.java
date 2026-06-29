package com.example.usercrud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
}
