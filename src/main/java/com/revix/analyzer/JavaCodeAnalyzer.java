package com.revix.analyzer;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JavaCodeAnalyzer implements CodeAnalyzer {

    @Override
    public List<Finding> analyze(String language, String code) {
        if (code == null || code.isBlank()) {
            return List.of(new Finding(
                    "VALIDATION",
                    "ERROR",
                    "Code is empty.",
                    "Provide Java code to analyze.",
                    null,
                    null,
                    null,
                    0.95
            ));
        }

        var rules = java.util.List.of(
                new com.revix.analyzer.rules.EmptyClassRule(),
                new com.revix.analyzer.rules.WeakClassNameRule(),
                new com.revix.analyzer.rules.TodoCommentRule()
        );

        return rules.stream()
                .map(r -> r.apply(code))
                .flatMap(java.util.Optional::stream)
                .toList();

    }
}
