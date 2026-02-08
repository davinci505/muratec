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

import java.time.LocalDate;

@Entity
@Table(name = "job_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_no")
    private String jobNo;

    @Column(name = "requester")
    private String requester;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "request_date")
    private LocalDate requestDate;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "factory_name")
    private String factoryName;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_spec")
    private String productSpec;

    @Column(name = "part_no")
    private String partNo;
}
