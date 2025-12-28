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

        // Step 2 will add real rules here
        return List.of();
    }
}
