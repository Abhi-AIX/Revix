package com.revix.analyzer.rules;

import com.revix.analyzer.Finding;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmptyClassRule implements Rule {

    private static final Pattern EMPTY_CLASS =
            Pattern.compile("\\bclass\\s+(\\w+)\\s*\\{\\s*\\}");

    @Override
    public Optional<Finding> apply(String code) {
        Matcher m = EMPTY_CLASS.matcher(code);
        if (!m.find()) return Optional.empty();

        String className = m.group(1);

        return Optional.of(new Finding(
                "BEST_PRACTICE",
                "WARNING",
                "Class '" + className + "' has an empty body. This can reduce clarity for reviewers.",
                "Add a brief comment or minimal method to clarify intent.",
                className + ".java",
                1,
                1,
                0.80
        ));
    }
}
