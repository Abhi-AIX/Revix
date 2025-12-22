package com.revix.api.analyze.dto;
import jakarta.validation.constraints.NotBlank;

public record AnalyzeRequest(
        @NotBlank String language,
        @NotBlank String code
) {}
