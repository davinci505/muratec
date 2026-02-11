package com.example.usercrud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "quote_parts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "margin_rate_id")
    private MarginRate marginRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_rate_id")
    private MarginRate expenseRate;

    @Column(name = "factory_name")
    private String factoryName;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_spec")
    private String productSpec;

    @Column(name = "part_no")
    private String partNo;

    @Column(name = "new_parts_no")
    private String newPartsNo;

    @Column(name = "model")
    private String model;

    @Column(name = "machine_name")
    private String machineName;

    @Column(name = "type")
    private String type;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "maker")
    private String maker;

    @Column(name = "murata_parts_no")
    private String murataPartsNo;

    @Column(name = "part_quantity")
    private Integer partQuantity;

    @Column(name = "quote_quantity")
    private Integer quoteQuantity;

    @Column(name = "unit_price_brt", precision = 15, scale = 2)
    private BigDecimal unitPriceBrt;

    @Column(name = "unit_price_hmx", precision = 15, scale = 2)
    private BigDecimal unitPriceHmx;

    @Column(name = "remark", length = 500)
    private String remark;

    @Transient
    public BigDecimal getMarginRateTotal() {
        return getCombinedRateTotal();
    }

    @Transient
    public BigDecimal getExpenseRateTotal() {
        if (expenseRate == null) {
            return null;
        }
        return expenseRate.getTotalRate();
    }

    @Transient
    public BigDecimal getMarginValueRate() {
        if (marginRate == null) {
            return null;
        }
        return marginRate.getMarginRate();
    }

    @Transient
    public BigDecimal getCombinedRateTotal() {
        return sumRates(getExpenseRateTotal(), getMarginValueRate());
    }

    @Transient
    public BigDecimal getUnitPriceBrtWithMargin() {
        return applyMargin(unitPriceBrt);
    }

    @Transient
    public BigDecimal getUnitPriceHmxWithMargin() {
        return applyMargin(unitPriceHmx);
    }

    private BigDecimal applyMargin(BigDecimal unitPrice) {
        if (unitPrice == null) {
            return null;
        }
        BigDecimal expenseRateTotal = getExpenseRateTotal();
        BigDecimal marginValueRate = getMarginValueRate();
        BigDecimal exchangeRate = getExchangeRate();
        if (expenseRateTotal == null || marginValueRate == null || exchangeRate == null) {
            return null;
        }
        if (BigDecimal.ZERO.compareTo(marginValueRate) == 0) {
            return null;
        }
        BigDecimal numerator = unitPrice
                .multiply(expenseRateTotal)
                .multiply(exchangeRate);
        BigDecimal result = numerator.divide(marginValueRate, 6, RoundingMode.HALF_UP);
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getExchangeRate() {
        if (marginRate == null) {
            return null;
        }
        return marginRate.getYenExchangeRate();
    }

    private BigDecimal sumRates(BigDecimal... values) {
        BigDecimal total = null;
        for (BigDecimal value : values) {
            if (value == null) {
                continue;
            }
            total = (total == null) ? value : total.add(value);
        }
        return total;
    }
}
