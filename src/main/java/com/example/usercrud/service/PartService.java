package com.example.usercrud.service;

import com.example.usercrud.model.Part;
import com.example.usercrud.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartService {
    
    @Autowired
    private PartRepository partRepository;
    
    public List<Part> getAllParts() {
        return partRepository.findAll();
    }
    
    public Optional<Part> getPartById(Long id) {
        return partRepository.findById(id);
    }
    
    public Part savePart(Part part) {
        return partRepository.save(part);
    }
    
    public void deletePart(Long id) {
        partRepository.deleteById(id);
    }
    
    public Part updatePart(Long id, Part partDetails) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found"));
        
        part.setPWbs(partDetails.getPWbs());
        part.setProjectCode(partDetails.getProjectCode());
        part.setSerNo(partDetails.getSerNo());
        part.setModel(partDetails.getModel());
        part.setMachineName(partDetails.getMachineName());
        part.setType(partDetails.getType());
        part.setNoOfMachine(partDetails.getNoOfMachine());
        part.setUnitName(partDetails.getUnitName());
        part.setMurataPartsNo(partDetails.getMurataPartsNo());
        part.setNewPartsNo(partDetails.getNewPartsNo());
        part.setDescriptionOfParts(partDetails.getDescriptionOfParts());
        part.setManufacturer(partDetails.getManufacturer());
        part.setManutPartsNo(partDetails.getManutPartsNo());
        part.setRank(partDetails.getRank());
        part.setNoOfPartsMachine(partDetails.getNoOfPartsMachine());
        part.setNoOfPartsTotal(partDetails.getNoOfPartsTotal());
        part.setUnitPrice(partDetails.getUnitPrice());
        
        return partRepository.save(part);
    }
}
