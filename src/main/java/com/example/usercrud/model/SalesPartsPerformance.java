package com.example.usercrud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales_parts_performance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesPartsPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer", length = 100)
    private String customer;

    @Column(name = "factory_name", length = 100)
    private String factoryName;

    @Column(name = "content", length = 500)
    private String content;

    @Column(name = "quote_no", length = 50)
    private String quoteNo;

    @Column(name = "quote_amount", precision = 15, scale = 2)
    private BigDecimal quoteAmount;

    @Column(name = "order_date_order_no", length = 100)
    private String orderDateOrderNo;

    @Column(name = "order_amount_excl_vat", precision = 15, scale = 2)
    private BigDecimal orderAmountExclVat;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "invoice_issue_date")
    private LocalDate invoiceIssueDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "po_no", length = 50)
    private String poNo;

    @Column(name = "order_amount", precision = 15, scale = 2)
    private BigDecimal orderAmount;

    @Column(name = "order_amount_jpy_to_krw", precision = 15, scale = 2)
    private BigDecimal orderAmountJpyToKrw;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "warehouse_receipt_date")
    private LocalDate warehouseReceiptDate;

    @Column(name = "invoice", length = 100)
    private String invoice;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "days_60_90", length = 20)
    private String days6090;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "murata_remittance_date")
    private LocalDate murataRemittanceDate;

    @Column(name = "shipping_company", length = 100)
    private String shippingCompany;

    @Column(name = "customs_duty", precision = 15, scale = 2)
    private BigDecimal customsDuty;

    @Column(name = "vat", precision = 15, scale = 2)
    private BigDecimal vat;

    @Column(name = "freight_customs", precision = 15, scale = 2)
    private BigDecimal freightCustoms;

    @Column(name = "total_import_cost", precision = 15, scale = 2)
    private BigDecimal totalImportCost;

    @Column(name = "net_profit", precision = 15, scale = 2)
    private BigDecimal netProfit;
}