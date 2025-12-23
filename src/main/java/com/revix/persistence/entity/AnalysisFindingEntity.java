package com.revix.persistence.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "analysis_finding")
public class AnalysisFindingEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "job_id", nullable = false)
    private UUID jobId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String severity;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(columnDefinition = "TEXT")
    private String suggestion;

    // maps to NUMERIC(3,2)
    private BigDecimal confidence;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "line_start")
    private Integer lineStart;

    @Column(name = "line_end")
    private Integer lineEnd;

    protected AnalysisFindingEntity() { /* JPA */ }

    public AnalysisFindingEntity(UUID id,
                                 UUID jobId,
                                 String category,
                                 String severity,
                                 String message) {
        this.id = id;
        this.jobId = jobId;
        this.category = category;
        this.severity = severity;
        this.message = message;
    }

    public UUID getId() { return id; }
    public UUID getJobId() { return jobId; }
    public String getCategory() { return category; }
    public String getSeverity() { return severity; }
    public String getMessage() { return message; }
    public String getSuggestion() { return suggestion; }
    public BigDecimal getConfidence() { return confidence; }
    public String getFilePath() { return filePath; }
    public Integer getLineStart() { return lineStart; }
    public Integer getLineEnd() { return lineEnd; }

    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setLineStart(Integer lineStart) { this.lineStart = lineStart; }
    public void setLineEnd(Integer lineEnd) { this.lineEnd = lineEnd; }
}
