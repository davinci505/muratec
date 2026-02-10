package com.example.usercrud.service;

import com.example.usercrud.model.JobRequest;
import com.example.usercrud.repository.JobRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobRequestService {

    @Autowired
    private JobRequestRepository jobRequestRepository;

    public List<JobRequest> getAllJobRequests() {
        return jobRequestRepository.findAll();
    }

    public List<JobRequest> searchByJobNoOrRequester(String query) {
        if (query == null || query.isBlank()) {
            return getAllJobRequests();
        }
        String keyword = query.trim();
        return jobRequestRepository.findByJobNoContainingIgnoreCaseOrRequesterContainingIgnoreCase(keyword, keyword);
    }

    public Optional<JobRequest> getJobRequestById(Long id) {
        return jobRequestRepository.findById(id);
    }

    public Optional<JobRequest> findByJobNo(String jobNo) {
        if (jobNo == null || jobNo.isBlank()) {
            return Optional.empty();
        }
        return jobRequestRepository.findByJobNo(jobNo.trim());
    }

    public JobRequest saveJobRequest(JobRequest jobRequest) {
        return jobRequestRepository.save(jobRequest);
    }

    public void deleteJobRequest(Long id) {
        jobRequestRepository.deleteById(id);
    }

    public JobRequest updateJobRequest(Long id, JobRequest details) {
        JobRequest jobRequest = jobRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobRequest not found"));

        jobRequest.setJobNo(details.getJobNo());
        jobRequest.setRequester(details.getRequester());
        jobRequest.setRequestDate(details.getRequestDate());
        jobRequest.setCustomerName(details.getCustomerName());
        jobRequest.setFactoryName(details.getFactoryName());
        jobRequest.setProductName(details.getProductName());
        jobRequest.setProductSpec(details.getProductSpec());
        jobRequest.setPartNo(details.getPartNo());

        return jobRequestRepository.save(jobRequest);
    }
}
