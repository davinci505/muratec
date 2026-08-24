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
import java.util.List;

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

    @Column(name = "factory_name")
    private String factoryName;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "part_no_product_spec")
    private String partNoProductSpec;

    @Column(name = "ccs_po_no")
    private String ccsPoNo;

    @Column(name = "work_no_serial_no")
    private String workNoSerialNo;

    @Column(name = "order_quantity")
    private Integer orderQuantity;

    @Column(name = "ccs_po_amount", precision = 15, scale = 2)
    private BigDecimal ccsPoAmount;

    @Column(name = "hmx_order_no")
    private String hmxOrderNo;

    @Column(name = "hmx_order_amount", precision = 15, scale = 2)
    private BigDecimal hmxOrderAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "delivery_date")
    private String deliveryDate;

    @Column(name = "remark", length = 500)
    private String remark;

    // Keep old fields for backward compatibility / margin calculation display
    @Transient
    private String productSpec;

    @Transient
    private String partNo;

    @Transient
    private String model;

    @Transient
    private Integer quoteQuantity;

    @Transient
    private BigDecimal unitPriceBrt;

    @Transient
    private BigDecimal unitPriceHmx;

    private static final BigDecimal HMX_MULTIPLIER = new BigDecimal("1.65");

    @Transient
    public BigDecimal getUnitPriceHmx() {
        if (ccsPoAmount == null) {
            return null;
        }
        return ccsPoAmount.multiply(HMX_MULTIPLIER).setScale(2, RoundingMode.HALF_UP);
    }

    @Transient
    private List<MarginRate> marginRatesForDisplay;

    public BigDecimal getDisplayRateValue(MarginRate rate) {
        if (rate == null) {
            return null;
        }
        return rate.getDisplayRateValue();
    }

    public BigDecimal getDisplayExchangeRate(MarginRate rate) {
        if (rate == null) {
            return null;
        }
        return rate.getYenExchangeRate();
    }

    public BigDecimal getDisplayUnitPriceWithMargin(MarginRate rate) {
        if (rate == null) {
            return null;
        }
        BigDecimal basePrice = rate.isHmxType() ? getUnitPriceHmx() : ccsPoAmount;
        return rate.calculateUnitPriceWithMargin(basePrice);
    }
}
