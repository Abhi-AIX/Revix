package com.revix.analyzer;

import org.springframework.stereotype.Service;

@Service
public class AnalyzerRouter {

    private final JavaCodeAnalyzer javaCodeAnalyzer;

    public AnalyzerRouter(JavaCodeAnalyzer javaCodeAnalyzer) {
        this.javaCodeAnalyzer = javaCodeAnalyzer;
    }

    public CodeAnalyzer route(String analyzerType) {
        // default
        if (analyzerType == null || analyzerType.isBlank()) {
            return javaCodeAnalyzer;
        }

        String normalized = analyzerType.trim().toUpperCase();

        return switch (normalized) {
            case "RULES" -> javaCodeAnalyzer;
            case "AI" -> throw new IllegalArgumentException("AI analyzer not implemented yet");
            default -> throw new IllegalArgumentException("Unsupported analyzerType: " + analyzerType);
        };
    }

    public String versionFor(String analyzerType) {
        if (analyzerType == null || analyzerType.isBlank()) return "rules-v1";

        return switch (analyzerType.trim().toUpperCase()) {
            case "RULES" -> "rules-v1";
            case "AI" -> "ai-v1";
            default -> "unknown";
        };
    }
}
