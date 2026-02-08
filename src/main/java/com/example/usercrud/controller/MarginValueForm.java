package com.example.usercrud.controller;

import java.math.BigDecimal;

public class MarginValueForm {

    private Long id;
    private String name;
    private BigDecimal yenExchangeRate;
    private BigDecimal marginRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getYenExchangeRate() {
        return yenExchangeRate;
    }

    public void setYenExchangeRate(BigDecimal yenExchangeRate) {
        this.yenExchangeRate = yenExchangeRate;
    }

    public BigDecimal getMarginRate() {
        return marginRate;
    }

    public void setMarginRate(BigDecimal marginRate) {
        this.marginRate = marginRate;
    }
}
