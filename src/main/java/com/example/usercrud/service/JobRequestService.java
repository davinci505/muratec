package com.example.usercrud.service;

import com.example.usercrud.model.JobRequest;
import com.example.usercrud.model.JobRequestPart;
import com.example.usercrud.model.Part;
import com.example.usercrud.repository.JobRequestRepository;
import com.example.usercrud.repository.PartRepository;

import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public List<JobRequest> searchByRequestNoOrRequester(String query) {
        if (query == null || !StringUtils.hasText(query)) {
            return getAllJobRequests();
        }
        String keyword = query.trim();
        return jobRequestRepository.findByRequestNoContainingIgnoreCaseOrRequesterContainingIgnoreCase(keyword, keyword);
    }

    @SuppressWarnings("null")
    public Optional<JobRequest> getJobRequestById(Long id) {
        return jobRequestRepository.findById(id);
    }

    public Optional<JobRequest> findByRequestNo(String requestNo) {
        if (requestNo == null || !StringUtils.hasText(requestNo)) {
            return Optional.empty();
        }
        return jobRequestRepository.findByRequestNo(requestNo.trim());
    }

    @SuppressWarnings("null")
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

    public void deleteJobRequest(@NonNull Long id) {
        jobRequestRepository.deleteById(id);
    }

    @SuppressWarnings("null")
    public JobRequest updateJobRequest(Long id, JobRequest details) {
        JobRequest jobRequest = jobRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobRequest not found"));

        jobRequest.setDivision(details.getDivision());
        jobRequest.setRequestNo(details.getRequestNo());
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
    @SuppressWarnings("null")
    public Page<JobRequest> getByFilters(String requestNo, String requester, String customerName, Pageable pageable) {
        boolean hasRequestNo = requestNo != null && StringUtils.hasText(requestNo);
        boolean hasRequester = requester != null && StringUtils.hasText(requester);
        boolean hasCustomerName = customerName != null && StringUtils.hasText(customerName);

        String rn = hasRequestNo ? requestNo.trim() : null;
        String req = hasRequester ? requester.trim() : null;
        String cn = hasCustomerName ? customerName.trim() : null;

        if (hasRequestNo && hasRequester && hasCustomerName) {
            return jobRequestRepository.findByRequestNoContainingIgnoreCaseOrRequesterContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(rn, req, cn, pageable);
        }
        if (hasRequestNo && hasRequester) {
            return jobRequestRepository.findByRequestNoContainingIgnoreCaseOrRequesterContainingIgnoreCase(rn, req, pageable);
        }
        if (hasRequestNo && hasCustomerName) {
            return jobRequestRepository.findByRequestNoContainingIgnoreCaseOrRequesterContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(rn, null, cn, pageable);
        }
        if (hasRequester && hasCustomerName) {
            return jobRequestRepository.findByRequestNoContainingIgnoreCaseOrRequesterContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(null, req, cn, pageable);
        }
        if (hasRequestNo) {
            return jobRequestRepository.findByRequestNoContainingIgnoreCase(rn, pageable);
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
