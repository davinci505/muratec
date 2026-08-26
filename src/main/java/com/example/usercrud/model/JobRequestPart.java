package com.example.usercrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "job_request_parts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequestPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_request_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private JobRequest jobRequest;

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

    // Convenience constructor for creating from Part
    public JobRequestPart(JobRequest jobRequest, Part part, int sortOrder) {
        this.jobRequest = jobRequest;
        this.part = part;
        this.partName = part.getPartName(); // Part's partName becomes partName
        this.partNumber = part.getPartNumber();
        this.spec = part.getSpec();
        this.quantity = 1;
        this.sortOrder = sortOrder;
    }
}