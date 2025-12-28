package com.revix.jobs;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.revix.analyzer.CodeAnalyzer;
import java.util.List;
import java.util.UUID;

@Service
public class AnalysisWorker {

    private final JobPersistenceService persistence;
    private final FindingPersistenceService findingPersistence;
    private final CodeAnalyzer analyzer;


    public AnalysisWorker(JobPersistenceService persistence,
                          FindingPersistenceService findingPersistence,
                          CodeAnalyzer analyzer) {
        this.persistence = persistence;
        this.findingPersistence = findingPersistence;
        this.analyzer = analyzer;
    }

    @Async
    public void process(UUID jobId, String language, String code) {
        try {
            persistence.markRunning(jobId);

            // simulate analysis (later: OpenAI call)
            Thread.sleep(5000);

            var analyzerFindings = analyzer.analyze(language, code);

            var entities = analyzerFindings.stream()
                    .map(f -> FindingPersistenceService.finding(
                            jobId,
                            f.category(),
                            f.severity(),
                            f.message(),
                            f.suggestion(),
                            new java.math.BigDecimal(String.valueOf(f.confidence())),
                            f.filePath(),
                            f.lineStart(),
                            f.lineEnd()
                    ))
                    .toList();

            findingPersistence.saveAll(entities);

            String summary = "Rules-based analysis complete. Findings=" + analyzerFindings.size();

            persistence.markDone(jobId, summary);
        } catch (Exception e) {
            persistence.markFailed(jobId, e.getMessage());
        }
    }
}
