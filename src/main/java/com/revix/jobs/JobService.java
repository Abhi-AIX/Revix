package com.revix.jobs;

import com.revix.jobs.model.AnalysisJob;
import com.revix.persistance.entity.AnalysisJobEntity;
import com.revix.persistance.repo.AnalysisJobRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobService {

    private final AnalysisJobRepository repo;

    public JobService(AnalysisJobRepository repo) {
        this.repo = repo;
    }

    public AnalysisJob createJob(String language, String code) {
        UUID id = UUID.randomUUID();

        AnalysisJobEntity entity = new AnalysisJobEntity(
                id,
                "PASTE",                 // source (Phase 1)
                JobStatus.PENDING.name(),
                language,
                Instant.now()
        );

        repo.save(entity);

        // We still return our domain model for now (contains code).
        // Later we will store the code safely and/or store only diff.
        return new AnalysisJob(id.toString(), language, code);
    }

    public Optional<AnalysisJob> getJob(String jobId) {
        UUID id;
        try {
            id = UUID.fromString(jobId);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }

        return repo.findById(id)
                .map(entity -> {
                    AnalysisJob job = new AnalysisJob(entity.getId().toString(), entity.getLanguage(), "");
                    job.setStatus(JobStatus.valueOf(entity.getStatus()));
                    return job;
                });
    }
}

