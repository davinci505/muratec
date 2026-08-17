package com.example.usercrud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

    @Column(name = "ccs_amount", precision = 15, scale = 2)
    private BigDecimal ccsAmount;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "brt_quote_no")
    private String brtQuoteNo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "brt_quote_date")
    private LocalDate brtQuoteDate;

    @Column(name = "brt_amount", precision = 15, scale = 2)
    private BigDecimal brtAmount;

    @Column(name = "brt_negotiated_amount", length = 500)
    private String brtNegotiatedAmount;

    @Column(name = "status")
    private String status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "status_date")
    private LocalDate statusDate;
}
