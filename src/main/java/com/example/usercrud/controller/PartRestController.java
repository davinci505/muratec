package com.example.usercrud.controller;

import com.example.usercrud.model.Part;
import com.example.usercrud.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
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
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order) {

        // Tabulator uses 1-based page, Spring uses 0-based
        int springPage = Math.max(0, page - 1);
        
        List<Part> parts = service.getByFilters(partNumber, description);
        
        // Manual pagination since service returns List
        int start = springPage * size;
        int end = Math.min(start + size, parts.size());
        List<Part> pageContent = (start < parts.size()) ? parts.subList(start, end) : List.of();

        long totalElements = parts.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        Map<String, Object> response = new HashMap<>();
        response.put("last_page", totalPages);
        response.put("data", pageContent.stream().map(this::toMap).collect(Collectors.toList()));
        
        return response;
    }

    private Map<String, Object> toMap(Part part) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", part.getId());
        map.put("partNumber", part.getPartNumber());
        map.put("description", part.getDescription());
        map.put("spec", part.getSpec());
        map.put("priceJpy", part.getPriceJpy());
        return map;
    }
}