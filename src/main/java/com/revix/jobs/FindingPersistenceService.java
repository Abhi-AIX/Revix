package com.revix.jobs;
import com.revix.persistance.entity.AnalysisFindingEntity;
import com.revix.persistance.repo.AnalysisFindingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class FindingPersistenceService {

    private final AnalysisFindingRepository repo;

    public FindingPersistenceService(AnalysisFindingRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void saveAll(List<AnalysisFindingEntity> findings) {
        repo.saveAll(findings);
    }

    public static AnalysisFindingEntity finding(UUID jobId,
                                                String category,
                                                String severity,
                                                String message,
                                                String suggestion,
                                                BigDecimal confidence,
                                                String filePath,
                                                Integer lineStart,
                                                Integer lineEnd) {
        AnalysisFindingEntity f = new AnalysisFindingEntity(
                UUID.randomUUID(),
                jobId,
                category,
                severity,
                message
        );
        f.setSuggestion(suggestion);
        f.setConfidence(confidence);
        f.setFilePath(filePath);
        f.setLineStart(lineStart);
        f.setLineEnd(lineEnd);
        return f;
    }
}
