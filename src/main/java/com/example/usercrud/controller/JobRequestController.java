package com.example.usercrud.controller;

import com.example.usercrud.model.JobRequest;
import com.example.usercrud.service.JobRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobRequestController {

    @Autowired
    private JobRequestService jobRequestService;

    @GetMapping
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobRequestService.getAllJobRequests());
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
    public String createJob(@ModelAttribute JobRequest job) {
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
        model.addAttribute("job", job);
        return "jobs/form";
    }

    @PostMapping("/{id}")
    public String updateJob(@PathVariable Long id, @ModelAttribute JobRequest job) {
        jobRequestService.updateJobRequest(id, job);
        return "redirect:/jobs";
    }

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobRequestService.deleteJobRequest(id);
        return "redirect:/jobs";
    }
}
