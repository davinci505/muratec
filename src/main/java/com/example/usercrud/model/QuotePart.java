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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
}
