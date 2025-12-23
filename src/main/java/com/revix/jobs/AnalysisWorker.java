package com.revix.jobs;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class AnalysisWorker {

    private final JobPersistenceService persistence;
    private final FindingPersistenceService findingPersistence;

    public AnalysisWorker(JobPersistenceService persistence,
                          FindingPersistenceService findingPersistence) {
        this.persistence = persistence;
        this.findingPersistence = findingPersistence;
    }

    @Async
    public void process(UUID jobId, String language, String code) {
        try {
            persistence.markRunning(jobId);

            // simulate analysis (later: OpenAI call)
            Thread.sleep(5000);
            var findings = List.of(
                    FindingPersistenceService.finding(
                            jobId,
                            "BEST_PRACTICE",
                            "WARNING",
                            "Avoid empty class bodies in examples; add at least one method or comment for intent.",
                            "Add a brief comment or a minimal method to clarify purpose.",
                            new java.math.BigDecimal("0.80"),
                            "A.java",
                            1,
                            1
                    ),
                    FindingPersistenceService.finding(
                            jobId,
                            "STYLE",
                            "INFO",
                            "Consider using a more descriptive class name than 'A'.",
                            "Rename 'A' to something meaningful (e.g., UserService, Analyzer).",
                            new java.math.BigDecimal("0.70"),
                            "A.java",
                            1,
                            1
                    )
            );

            findingPersistence.saveAll(findings);

            String summary = "Simulated analysis: language=" + language
                    + ", codeLength=" + (code == null ? 0 : code.length());

            persistence.markDone(jobId, summary);
        } catch (Exception e) {
            persistence.markFailed(jobId, e.getMessage());
        }
    }
}
