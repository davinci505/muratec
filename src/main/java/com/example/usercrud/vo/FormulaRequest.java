package com.example.usercrud.vo;

import com.example.usercrud.model.FormulaItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FormulaRequest {

    private String code;
    private String description;
    private List<Item> items;

    @Data
    public static class Item {
        private String itemKey;
        private String itemName;
        private BigDecimal multiplier;
        private Integer sortOrder;
    }
}
