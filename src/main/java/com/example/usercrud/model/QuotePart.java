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
    @JoinColumn(name = "brt_margin_rate_id")
    private MarginRate brtMarginRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hmx_margin_rate_id")
    private MarginRate hmxMarginRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_rate_id")
    private MarginRate expenseRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brt_expense_rate_id")
    private MarginRate brtExpenseRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hmx_expense_rate_id")
    private MarginRate hmxExpenseRate;

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

    @Transient
    private BigDecimal unitPriceHmx;

    private static final BigDecimal HMX_MULTIPLIER = new BigDecimal("1.65");

    @Column(name = "remark", length = 500)
    private String remark;

    @Transient
    public BigDecimal getMarginRateTotal() {
        return getCombinedRateTotal();
    }

    @Transient
    public BigDecimal getExpenseRateTotal() {
        return getBrtExpenseRateTotal();
    }

    @Transient
    public BigDecimal getBrtExpenseRateTotal() {
        MarginRate rate = getBrtExpenseRateRef();
        return rate == null ? null : rate.getTotalRate();
    }

    @Transient
    public BigDecimal getHmxExpenseRateTotal() {
        MarginRate rate = getHmxExpenseRateRef();
        return rate == null ? null : rate.getTotalRate();
    }

    @Transient
    public BigDecimal getMarginValueRate() {
        return getBrtMarginValueRate();
    }

    @Transient
    public BigDecimal getBrtMarginValueRate() {
        MarginRate rate = getBrtMarginRateRef();
        return rate == null ? null : rate.getMarginRate();
    }

    @Transient
    public BigDecimal getHmxMarginValueRate() {
        MarginRate rate = getHmxMarginRateRef();
        return rate == null ? null : rate.getMarginRate();
    }

    @Transient
    public BigDecimal getExchangeRateValue() {
        return getBrtExchangeRateValue();
    }

    @Transient
    public BigDecimal getBrtExchangeRateValue() {
        MarginRate rate = getBrtMarginRateRef();
        return rate == null ? null : rate.getYenExchangeRate();
    }

    @Transient
    public BigDecimal getHmxExchangeRateValue() {
        MarginRate rate = getHmxMarginRateRef();
        return rate == null ? null : rate.getYenExchangeRate();
    }

    @Transient
    public BigDecimal getCombinedRateTotal() {
        return sumRates(getExpenseRateTotal(), getMarginValueRate());
    }

    @Transient
    public BigDecimal getUnitPriceBrtWithMargin() {
        return applyMarginBrt(unitPriceBrt, getBrtExpenseRateRef());
    }

    @Transient
    public BigDecimal getUnitPriceHmxWithMargin() {
        return applyMarginHmx(getUnitPriceHmx(), getHmxMarginRateRef());
    }

    @Transient
    public BigDecimal getUnitPriceHmx() {
        if (unitPriceBrt == null) {
            return null;
        }
        return unitPriceBrt.multiply(HMX_MULTIPLIER).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal applyMarginBrt(BigDecimal unitPrice, MarginRate expenseRateRef) {
        if (unitPrice == null || BigDecimal.ZERO.compareTo(unitPrice) == 0) {
            return null;
        }
        if (expenseRateRef == null) {
            return null;
        }
        BigDecimal transportClearanceRate = expenseRateRef.getTransportClearanceRate();
        BigDecimal exchangeRate = expenseRateRef.getYenExchangeRate();
        if (transportClearanceRate == null || exchangeRate == null) {
            return null;
        }
        if (BigDecimal.ZERO.compareTo(exchangeRate) == 0) {
            return null;
        }
        BigDecimal result = unitPrice
                .multiply(transportClearanceRate)
                .multiply(exchangeRate);
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal applyMarginHmx(BigDecimal unitPrice, MarginRate marginRateRef) {
        if (unitPrice == null || BigDecimal.ZERO.compareTo(unitPrice) == 0) {
            return null;
        }
        if (marginRateRef == null) {
            return null;
        }
        BigDecimal marginValueRate = marginRateRef.getMarginRate();
        BigDecimal exchangeRate = marginRateRef.getYenExchangeRate();
        if (marginValueRate == null || exchangeRate == null) {
            return null;
        }
        if (BigDecimal.ZERO.compareTo(marginValueRate) == 0 || BigDecimal.ZERO.compareTo(exchangeRate) == 0) {
            return null;
        }
        BigDecimal numerator = unitPrice.multiply(exchangeRate);
        BigDecimal result = numerator.divide(marginValueRate, 6, RoundingMode.HALF_UP);
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    private MarginRate getBrtMarginRateRef() {
        return brtMarginRate != null ? brtMarginRate : marginRate;
    }

    private MarginRate getHmxMarginRateRef() {
        if (hmxMarginRate != null) {
            return hmxMarginRate;
        }
        if (brtMarginRate != null) {
            return brtMarginRate;
        }
        return marginRate;
    }

    private MarginRate getBrtExpenseRateRef() {
        return brtExpenseRate != null ? brtExpenseRate : expenseRate;
    }

    private MarginRate getHmxExpenseRateRef() {
        if (hmxExpenseRate != null) {
            return hmxExpenseRate;
        }
        if (brtExpenseRate != null) {
            return brtExpenseRate;
        }
        return expenseRate;
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
