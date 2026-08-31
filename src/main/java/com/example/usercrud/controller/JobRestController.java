package com.example.usercrud.controller;

import com.example.usercrud.model.JobRequest;
import com.example.usercrud.service.JobRequestService;
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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobRestController {

    @Autowired
    private JobRequestService service;

    @GetMapping
    public Map<String, Object> getJobs(
            @RequestParam(value = "requestNo", required = false) String requestNo,
            @RequestParam(value = "requester", required = false) String requester,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order) {

        // Tabulator uses 1-based page, Spring uses 0-based
        int springPage = Math.max(0, page - 1);
        Pageable pageable = PageRequest.of(springPage, size,
            "asc".equalsIgnoreCase(order) ?
                Sort.by(sort).ascending() :
                Sort.by(sort).descending());

        Page<JobRequest> jobPage = service.getByFilters(requestNo, requester, customerName, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("last_page", jobPage.getTotalPages());
        response.put("data", jobPage.getContent().stream().map(this::toMap).collect(Collectors.toList()));

        return response;
    }

    private Map<String, Object> toMap(JobRequest job) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", job.getId());
        map.put("requestNo", job.getRequestNo());
        map.put("division", job.getDivision());
        map.put("requester", job.getRequester());
        map.put("requestDate", job.getRequestDate() != null ? job.getRequestDate().toString() : "");
        map.put("customerName", job.getCustomerName());
        map.put("factoryName", job.getFactoryName());
        map.put("partsCount", job.getParts() != null ? job.getParts().size() : 0);
        return map;
    }
}