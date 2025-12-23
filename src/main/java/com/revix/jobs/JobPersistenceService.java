package com.revix.jobs;

import com.revix.persistence.entity.AnalysisJobEntity;
import com.revix.persistence.repo.AnalysisJobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class JobPersistenceService {

    private final AnalysisJobRepository repo;

    public JobPersistenceService(AnalysisJobRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void markRunning(UUID jobId) {
        AnalysisJobEntity job = repo.findById(jobId).orElseThrow();
        job.setStatus(JobStatus.RUNNING.name());
        job.setStartedAt(Instant.now());
    }

    @Transactional
    public void markDone(UUID jobId, String summary) {
        AnalysisJobEntity job = repo.findById(jobId).orElseThrow();
        job.setStatus(JobStatus.DONE.name());
        job.setSummary(summary);
        job.setFinishedAt(Instant.now());
    }

    @Transactional
    public void markFailed(UUID jobId, String errorMessage) {
        AnalysisJobEntity job = repo.findById(jobId).orElseThrow();
        job.setStatus(JobStatus.FAILED.name());
        job.setErrorMessage(errorMessage);
        job.setFinishedAt(Instant.now());
    }
}
