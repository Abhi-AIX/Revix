package com.revix.analyzer;

public record Finding(
        String category,
        String severity,
        String message,
        String suggestion,
        String filePath,
        Integer lineStart,
        Integer lineEnd,
        double confidence
) { }
