package com.revix.analyzer;
import java.util.List;

public interface CodeAnalyzer {
    List<Finding> analyze(String language, String code);
}

