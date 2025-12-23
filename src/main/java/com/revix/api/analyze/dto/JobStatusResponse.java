package com.revix.api.analyze.dto;

public record JobStatusResponse(
        String jobId,
        String status,
        String summary,
        String errorMessage,
        java.util.List<FindingResponse> findings
) {}
