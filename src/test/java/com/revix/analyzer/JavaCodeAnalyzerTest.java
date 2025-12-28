package com.revix.analyzer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JavaCodeAnalyzerTest {

    private final JavaCodeAnalyzer analyzer = new JavaCodeAnalyzer();

    @Test
    void detectsTodo() {
        var findings = analyzer.analyze("java", "public class A { // TODO: fix }");
        assertThat(findings).anyMatch(f -> f.category().equals("CODE_SMELL"));
    }
}
