package com.revix.analyzer.rules;

import com.revix.analyzer.Finding;

import java.util.Optional;
import java.util.regex.Pattern;

public class TodoCommentRule implements Rule {

    private static final Pattern TODO = Pattern.compile("(?i)\\bTODO\\b");

    @Override
    public Optional<Finding> apply(String code) {
        if (code == null || code.isBlank()) return Optional.empty();
        if (!TODO.matcher(code).find()) return Optional.empty();

        return Optional.of(new Finding(
                "CODE_SMELL",
                "INFO",
                "TODO comment detected. Consider resolving it or tracking it in an issue.",
                "Replace TODO with a ticket reference (e.g., JIRA-123) or implement before merge.",
                null,
                null,
                null,
                0.65
        ));
    }
}
