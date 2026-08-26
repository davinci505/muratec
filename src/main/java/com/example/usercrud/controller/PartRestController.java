package com.example.usercrud.controller;

import com.example.usercrud.model.Part;
import com.example.usercrud.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parts")
public class PartRestController {

    @Autowired
    private PartService service;

    @GetMapping
    public Map<String, Object> getParts(
            @RequestParam(value = "partNumber", required = false) String partNumber,
            @RequestParam(value = "partName", required = false) String partName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order) {

        // Tabulator uses 1-based page, Spring uses 0-based
        int springPage = Math.max(0, page - 1);
        Pageable pageable = PageRequest.of(springPage, size, 
            "asc".equalsIgnoreCase(order) ? 
                org.springframework.data.domain.Sort.by(sort).ascending() : 
                org.springframework.data.domain.Sort.by(sort).descending());

        Page<Part> partPage = service.getByFilters(partNumber, partName, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("last_page", partPage.getTotalPages());
        response.put("data", partPage.getContent().stream().map(this::toMap).collect(Collectors.toList()));
        
        return response;
    }

    private Map<String, Object> toMap(Part part) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", part.getId());
        map.put("partNumber", part.getPartNumber());
        map.put("partName", part.getPartName());
        map.put("spec", part.getSpec());
        map.put("priceJpy", part.getPriceJpy());
        return map;
    }
}