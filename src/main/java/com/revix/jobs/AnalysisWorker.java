package com.revix.jobs;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

public class AnalysisWorker {

    private final JobPersistenceService persistence;

    public AnalysisWorker(JobPersistenceService persistence) {
        this.persistence = persistence;
    }

    @Async
    public void process(UUID jobId, String language, String code) {
        try {
            persistence.markRunning(jobId);

            // simulate analysis (later: OpenAI call)
            Thread.sleep(2000);

            String summary = "Simulated analysis: language=" + language
                    + ", codeLength=" + (code == null ? 0 : code.length());

            persistence.markDone(jobId, summary);
        } catch (Exception e) {
            persistence.markFailed(jobId, e.getMessage());
        }
    }
}
