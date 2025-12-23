package com.revix.jobs.model;
import com.revix.jobs.JobStatus;
import java.time.Instant;

public class AnalysisJob {

    private final String id;
    private final String language;
    private final String code;
    private final Instant createdAt;
    private String summary;
    private String errorMessage;
    private JobStatus status;

    public AnalysisJob(String id, String language, String code) {
        this.id = id;
        this.language = language;
        this.code = code;
        this.createdAt = Instant.now();
        this.status = JobStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getCode() {
        return code;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
