package com.example.usercrud.service;

import com.example.usercrud.model.JobRequest;
import com.example.usercrud.model.JobRequestPart;
import com.example.usercrud.model.Part;
import com.example.usercrud.repository.JobRequestRepository;
import com.example.usercrud.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobRequestService {

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private JobRequestPartService jobRequestPartService;

    @Autowired
    private PartRepository partRepository;

    public List<JobRequest> getAllJobRequests() {
        return jobRequestRepository.findAll();
    }

    public List<JobRequest> searchByJobNoOrRequester(String query) {
        if (query == null || !StringUtils.hasText(query)) {
            return getAllJobRequests();
        }
        String keyword = query.trim();
        return jobRequestRepository.findByJobNoContainingIgnoreCaseOrRequesterContainingIgnoreCase(keyword, keyword);
    }

    public Optional<JobRequest> getJobRequestById(Long id) {
        return jobRequestRepository.findById(id);
    }

    public Optional<JobRequest> findByJobNo(String jobNo) {
        if (jobNo == null || !StringUtils.hasText(jobNo)) {
            return Optional.empty();
        }
        return jobRequestRepository.findByJobNo(jobNo.trim());
    }

    public JobRequest saveJobRequest(JobRequest jobRequest) {
        JobRequest saved = jobRequestRepository.save(jobRequest);
        // Save parts if any
        if (jobRequest.getParts() != null && !jobRequest.getParts().isEmpty()) {
            for (int i = 0; i < jobRequest.getParts().size(); i++) {
                JobRequestPart part = jobRequest.getParts().get(i);
                part.setSortOrder(i);
                part.setJobRequest(saved);
                // Set Part reference from partNumber
                if (part.getPartNumber() != null) {
                    Part partEntity = partRepository.findByPartNumber(part.getPartNumber()).orElse(null);
                    if (partEntity != null) {
                        part.setPart(partEntity);
                    }
                }
            }
            jobRequestPartService.saveAll(jobRequest.getParts());
        }
        return saved;
    }

    public void deleteJobRequest(Long id) {
        jobRequestRepository.deleteById(id);
    }

    public JobRequest updateJobRequest(Long id, JobRequest details) {
        JobRequest jobRequest = jobRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobRequest not found"));

        jobRequest.setDivision(details.getDivision());
        jobRequest.setJobNo(details.getJobNo());
        jobRequest.setRequester(details.getRequester());
        jobRequest.setRequestDate(details.getRequestDate());
        jobRequest.setCustomerName(details.getCustomerName());
        jobRequest.setFactoryName(details.getFactoryName());

        // Handle parts - clear existing and add new
        jobRequest.clearParts();
        if (details.getParts() != null && !details.getParts().isEmpty()) {
            for (int i = 0; i < details.getParts().size(); i++) {
                JobRequestPart part = details.getParts().get(i);
                part.setSortOrder(i);
                jobRequest.addPart(part);
                // Set Part reference from partNumber
                if (part.getPartNumber() != null) {
                    Part partEntity = partRepository.findByPartNumber(part.getPartNumber()).orElse(null);
                    if (partEntity != null) {
                        part.setPart(partEntity);
                    }
                }
            }
        }

        return jobRequestRepository.save(jobRequest);
    }

    public List<JobRequestPart> getPartsByJobRequestId(Long jobRequestId) {
        return jobRequestPartService.findByJobRequestId(jobRequestId);
    }

    // Pagination support for Tabulator
    public Page<JobRequest> getByFilters(String jobNo, String requester, String customerName, Pageable pageable) {
        boolean hasJobNo = jobNo != null && StringUtils.hasText(jobNo);
        boolean hasRequester = requester != null && StringUtils.hasText(requester);
        boolean hasCustomerName = customerName != null && StringUtils.hasText(customerName);

        String jn = hasJobNo ? jobNo.trim() : null;
        String req = hasRequester ? requester.trim() : null;
        String cn = hasCustomerName ? customerName.trim() : null;

        if (hasJobNo && hasRequester && hasCustomerName) {
            return jobRequestRepository.findByJobNoContainingIgnoreCaseOrRequesterContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(jn, req, cn, pageable);
        }
        if (hasJobNo && hasRequester) {
            return jobRequestRepository.findByJobNoContainingIgnoreCaseOrRequesterContainingIgnoreCase(jn, req, pageable);
        }
        if (hasJobNo && hasCustomerName) {
            return jobRequestRepository.findByJobNoContainingIgnoreCaseOrRequesterContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(jn, null, cn, pageable);
        }
        if (hasRequester && hasCustomerName) {
            return jobRequestRepository.findByJobNoContainingIgnoreCaseOrRequesterContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(null, req, cn, pageable);
        }
        if (hasJobNo) {
            return jobRequestRepository.findByJobNoContainingIgnoreCase(jn, pageable);
        }
        if (hasRequester) {
            return jobRequestRepository.findByRequesterContainingIgnoreCase(req, pageable);
        }
        if (hasCustomerName) {
            return jobRequestRepository.findByCustomerNameContainingIgnoreCase(cn, pageable);
        }
        return jobRequestRepository.findAll(pageable);
    }
}
