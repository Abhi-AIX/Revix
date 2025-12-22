package com.revix.jobs.model;
import com.revix.jobs.JobStatus;
import java.time.Instant;

public class AnalysisJob {

    private final String id;
    private final String language;
    private final String code;
    private final Instant createdAt;
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

}
