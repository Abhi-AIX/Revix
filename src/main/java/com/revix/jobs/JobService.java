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
    private final AnalysisWorker worker;

    public JobService(AnalysisJobRepository repo, AnalysisWorker worker) {
        this.repo = repo;
        this.worker = worker;
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
        worker.process(id, language, code);

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
                    AnalysisJob job = new AnalysisJob(
                            entity.getId().toString(),
                            entity.getLanguage(),
                            ""
                    );

                    job.setStatus(JobStatus.valueOf(entity.getStatus()));
                    job.setSummary(entity.getSummary());
                    job.setErrorMessage(entity.getErrorMessage());

                    return job;
                });
    }
}

