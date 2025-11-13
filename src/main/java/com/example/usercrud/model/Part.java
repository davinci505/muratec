package com.example.usercrud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "parts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Part {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonProperty("pWbs")
    @Column(name = "p_wbs")
    private String pWbs;
    
    @JsonProperty("projectCode")
    @Column(name = "project_code")
    private String projectCode;
    
    @JsonProperty("serNo")
    @Column(name = "ser_no")
    private String serNo;
    
    private String model;
    
    @JsonProperty("machineName")
    @Column(name = "machine_name")
    private String machineName;
    
    private String type;
    
    @JsonProperty("noOfMachine")
    @Column(name = "no_of_machine")
    private Integer noOfMachine;
    
    @JsonProperty("unitName")
    @Column(name = "unit_name")
    private String unitName;
    
    @JsonProperty("murataPartsNo")
    @Column(name = "murata_parts_no")
    private String murataPartsNo;
    
    @JsonProperty("newPartsNo")
    @Column(name = "new_parts_no")
    private String newPartsNo;
    
    @JsonProperty("descriptionOfParts")
    @Column(name = "description_of_parts", length = 500)
    private String descriptionOfParts;
    
    private String manufacturer;
    
    @JsonProperty("manutPartsNo")
    @Column(name = "manut_parts_no")
    private String manutPartsNo;
    
    @Column(name = "rank")
    private String rank;
    
    @JsonProperty("noOfPartsMachine")
    @Column(name = "no_of_parts_machine")
    private Integer noOfPartsMachine;
    
    @JsonProperty("noOfPartsTotal")
    @Column(name = "no_of_parts_total")
    private Integer noOfPartsTotal;
    
    @JsonProperty("unitPrice")
    @Column(name = "unit_price")
    private String unitPrice;
}
