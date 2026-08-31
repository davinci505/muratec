package com.example.usercrud.controller;

import com.example.usercrud.model.JobRequest;
import com.example.usercrud.model.JobRequestPart;
import com.example.usercrud.service.JobRequestPartService;
import com.example.usercrud.service.JobRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobRequestController {

    @Autowired
    private JobRequestService jobRequestService;

    @Autowired
    private JobRequestPartService jobRequestPartService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public String listJobs(@RequestParam(value = "q", required = false) String query, Model model) {
        model.addAttribute("jobs", jobRequestService.searchByRequestNoOrRequester(query));
        model.addAttribute("q", query);
        return "jobs/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("job", new JobRequest());
        return "jobs/form";
    }

    @GetMapping("/bulk")
    public String showBulkCreateForm() {
        return "jobs/bulk-form";
    }

    @PostMapping
    public String createJob(@ModelAttribute JobRequest job, 
                            @RequestParam(value = "partsJson", required = false) String partsJson,
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 입력하세요.");
            return "jobs/form";
        }
        
        // Parse parts from JSON
        if (partsJson != null && !partsJson.isEmpty()) {
            try {
                List<JobRequestPart> parts = objectMapper.readValue(partsJson, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, JobRequestPart.class));
                // Set back-reference to jobRequest for each part
                for (JobRequestPart part : parts) {
                    job.addPart(part);
                }
            } catch (Exception e) {
                model.addAttribute("errorMessage", "부품 데이터 파싱 오류: " + e.getMessage());
                return "jobs/form";
            }
        }
        
        jobRequestService.saveJobRequest(job);
        return "redirect:/jobs";
    }

    @PostMapping("/bulk")
    @ResponseBody
    public String createBulkJobs(@RequestBody List<JobRequest> jobs) {
        for (JobRequest job : jobs) {
            jobRequestService.saveJobRequest(job);
        }
        return "success";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        JobRequest job = jobRequestService.getJobRequestById(id)
                .orElseThrow(() -> new RuntimeException("JobRequest not found"));
        
        // Load parts for this job request
        List<JobRequestPart> parts = jobRequestPartService.findByJobRequestId(id);
        job.setParts(parts);
        
        model.addAttribute("job", job);
        return "jobs/form";
    }

    @PostMapping("/{id}")
    public String updateJob(@PathVariable Long id, @ModelAttribute JobRequest job,
                            @RequestParam(value = "partsJson", required = false) String partsJson,
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 입력하세요.");
            return "jobs/form";
        }
        
        // Parse parts from JSON
        if (partsJson != null && !partsJson.isEmpty()) {
            try {
                List<JobRequestPart> parts = objectMapper.readValue(partsJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, JobRequestPart.class));
                // Clear existing parts and add new ones with back-reference
                job.clearParts();
                for (JobRequestPart part : parts) {
                    job.addPart(part);
                }
            } catch (Exception e) {
                model.addAttribute("errorMessage", "부품 데이터 파싱 오류: " + e.getMessage());
                return "jobs/form";
            }
        } else {
            job.clearParts();
        }
        
        jobRequestService.updateJobRequest(id, job);
        return "redirect:/jobs";
    }

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobRequestService.deleteJobRequest(id);
        return "redirect:/jobs";
    }
}
