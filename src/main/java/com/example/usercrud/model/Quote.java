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
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "quotes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_request_id")
    private JobRequest jobRequest;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "ccs_quote_date")
    private LocalDate ccsQuoteDate;

    @Column(name = "ccs_quote_no")
    private String ccsQuoteNo;

    @Column(name = "ccs_amount_brt", precision = 15, scale = 2)
    private BigDecimal ccsAmountBrt;

    @Column(name = "ccs_amount_hmx", precision = 15, scale = 2)
    private BigDecimal ccsAmountHmx;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "brt_quote_no")
    private String brtQuoteNo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "brt_quote_date")
    private LocalDate brtQuoteDate;

    @Column(name = "brt_negotiated_amount", precision = 15, scale = 2)
    private BigDecimal brtNegotiatedAmount;

    @Column(name = "status")
    private String status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "status_date")
    private LocalDate statusDate;

    @Column(name = "hmx_order_no")
    private String hmxOrderNo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "hmx_order_date")
    private LocalDate hmxOrderDate;

    @Column(name = "ccs_po_no")
    private String ccsPoNo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "ccs_po_date")
    private LocalDate ccsPoDate;
}
