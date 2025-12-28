package com.revix.analyzer.rules;

import com.revix.analyzer.Finding;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeakClassNameRule implements Rule {

    private static final Pattern CLASS_NAME =
            Pattern.compile("\\bclass\\s+(\\w+)\\b");

    @Override
    public Optional<Finding> apply(String code) {
        Matcher m = CLASS_NAME.matcher(code);
        if (!m.find()) return Optional.empty();

        String className = m.group(1);

        if (className.length() > 1) return Optional.empty();

        return Optional.of(new Finding(
                "STYLE",
                "INFO",
                "Class name '" + className + "' is not descriptive.",
                "Rename '" + className + "' to a meaningful name (e.g., UserService, Analyzer).",
                className + ".java",
                1,
                1,
                0.70
        ));
    }
}
