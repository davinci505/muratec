package com.example.usercrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "division")
    private String division;

    @Column(name = "requester")
    private String requester;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "request_date")
    private LocalDate requestDate;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "factory_name")
    private String factoryName;

    @OneToMany(mappedBy = "jobRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<JobRequestPart> parts = new ArrayList<>();

    // Helper methods for managing parts
    public void addPart(JobRequestPart part) {
        parts.add(part);
        part.setJobRequest(this);
    }

    public void removePart(JobRequestPart part) {
        parts.remove(part);
        part.setJobRequest(null);
    }

    public void clearParts() {
        parts.clear();
    }
}
