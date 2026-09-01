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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
    name = "formula_items",
    uniqueConstraints = @UniqueConstraint(name = "uk_formula_item_key", columnNames = {"formula_id", "item_key"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormulaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "formula_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Formula formula;

    @Column(name = "item_key", length = 50, nullable = false)
    private String itemKey;

    @Column(name = "item_name", length = 200, nullable = false)
    private String itemName;

    @Column(name = "multiplier", precision = 20, scale = 6, nullable = false)
    private BigDecimal multiplier;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
