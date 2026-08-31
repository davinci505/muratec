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
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "quote_parts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quote_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Quote quote;

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = true)
    @ToString.Exclude
    @JsonIgnore
    private Part part;

    @Column(name = "part_name", length = 200)
    private String partName;

    @Column(name = "part_number", length = 50)
    private String partNumber;

    @Column(name = "spec", length = 500)
    private String spec;

    @Column(name = "quantity")
    private Integer quantity = 1;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "purchase_price", precision = 15, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "selling_price", precision = 15, scale = 2)
    private BigDecimal sellingPrice;

    // Convenience constructor for creating from Part
    public QuotePart(Quote quote, Part part, int sortOrder) {
        this.quote = quote;
        this.part = part;
        this.partName = part.getPartName(); // Part's partName becomes partName
        this.partNumber = part.getPartNumber();
        this.spec = part.getSpec();
        this.quantity = 1;
        this.sortOrder = sortOrder;
    }
}
