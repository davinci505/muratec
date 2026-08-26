package com.example.usercrud.service;

import com.example.usercrud.model.JobRequestPart;
import com.example.usercrud.repository.JobRequestPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobRequestPartService {

    @Autowired
    private JobRequestPartRepository jobRequestPartRepository;

    public List<JobRequestPart> findByJobRequestId(Long jobRequestId) {
        return jobRequestPartRepository.findByJobRequestId(jobRequestId);
    }

    public void saveAll(List<JobRequestPart> parts) {
        jobRequestPartRepository.saveAll(parts);
    }

    public void deleteByJobRequestId(Long jobRequestId) {
        jobRequestPartRepository.deleteAll(findByJobRequestId(jobRequestId));
    }
}