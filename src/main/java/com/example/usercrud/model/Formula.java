package com.example.usercrud.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "formulas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(
            mappedBy = "formula",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("sortOrder ASC, id ASC")
    @ToString.Exclude
    private List<FormulaItem> items = new ArrayList<>();

    /**
     * 양방향 연관관계 편의 메서드. items 추가 시 formula 참조를 자동으로 연결합니다.
     */
    public void addItem(FormulaItem item) {
        if (item == null) return;
        item.setFormula(this);
        this.items.add(item);
    }

    public void clearItems() {
        this.items.clear();
    }

    /**
     * 곱셈 계산: sourceValue × items[0].multiplier × items[1].multiplier × ...
     * sortOrder 순서대로 모든 항목의 multiplier를 곱합니다.
     */
    @Transient
    public BigDecimal calculate(BigDecimal sourceValue) {
        if (sourceValue == null) {
            return null;
        }
        if (items == null || items.isEmpty()) {
            return sourceValue;
        }
        List<FormulaItem> ordered = new ArrayList<>(items);
        ordered.sort(Comparator.comparing(
                FormulaItem::getSortOrder,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));
        BigDecimal result = sourceValue;
        for (FormulaItem item : ordered) {
            if (item.getMultiplier() == null) {
                continue;
            }
            result = result.multiply(item.getMultiplier());
        }
        return result;
    }
}
