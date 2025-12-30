package com.revix.jobs;

import com.revix.jobs.model.AnalysisJob;
import com.revix.persistence.entity.AnalysisJobEntity;
import com.revix.persistence.repo.AnalysisJobRepository;
import org.springframework.stereotype.Service;
import com.revix.analyzer.AnalyzerType;


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

    public AnalysisJob createJob(String language, String code, String analyzerTypeRaw) {
        UUID id = UUID.randomUUID();
        String analyzerType = (analyzerTypeRaw == null || analyzerTypeRaw.isBlank())
                ? "RULES"
                : analyzerTypeRaw.trim().toUpperCase();
        String analyzerVersion = analyzerType.equals("RULES") ? "rules-v1" : "ai-v1";

        AnalysisJobEntity entity = new AnalysisJobEntity(
                id,
                "PASTE",                 // source (Phase 1)
                JobStatus.PENDING.name(),
                language,
                Instant.now()
        );

        entity.setAnalyzerType(analyzerType);
        entity.setAnalyzerVersion(analyzerVersion);
        repo.save(entity);
        worker.process(id, language, code, analyzerType);
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
                    job.setAnalyzerType(AnalyzerType.valueOf(entity.getAnalyzerType()));
                    job.setAnalyzerVersion(entity.getAnalyzerVersion());

                    return job;
                });
    }
}

