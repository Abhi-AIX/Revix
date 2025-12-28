package com.revix.jobs;

import com.revix.persistance.repo.AnalysisFindingRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class FindingQueryService {

    private final AnalysisFindingRepository repo;

    public FindingQueryService(AnalysisFindingRepository repo) {
        this.repo = repo;
    }

    public java.util.List<com.revix.persistance.entity.AnalysisFindingEntity> findByJobId(UUID jobId) {
        return repo.findByJobId(jobId);
    }
}
