package com.revix.persistence.entity;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "analysis_job")
public class AnalysisJobEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String language;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    protected AnalysisJobEntity() {
        // JPA
    }

    public AnalysisJobEntity(UUID id, String source, String status, String language, Instant createdAt) {
        this.id = id;
        this.source = source;
        this.status = status;
        this.language = language;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getSource() { return source; }
    public String getStatus() { return status; }
    public String getLanguage() { return language; }
    public Instant getCreatedAt() { return createdAt; }
    public String getSummary() { return summary; }
    public String getErrorMessage() { return errorMessage; }

    public void setStatus(String status) { this.status = status; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
    public void setFinishedAt(Instant finishedAt) { this.finishedAt = finishedAt; }
}

