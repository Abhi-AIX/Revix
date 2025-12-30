package com.revix.jobs;

import com.revix.persistence.repo.AnalysisFindingRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class FindingQueryService {

    private final AnalysisFindingRepository repo;

    public FindingQueryService(AnalysisFindingRepository repo) {
        this.repo = repo;
    }

    public java.util.List<com.revix.persistence.entity.AnalysisFindingEntity> findByJobId(UUID jobId) {
        return repo.findByJobId(jobId);
    }
}
