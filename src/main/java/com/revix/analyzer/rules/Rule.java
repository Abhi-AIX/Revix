package com.revix.analyzer.rules;

import com.revix.analyzer.Finding;

import java.util.Optional;

public interface Rule {
    Optional<Finding> apply(String code);
}
