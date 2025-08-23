package com.github.mahdim1000.exceptionhandling.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Utility class for generating trace IDs for error tracking.
 * Provides unique identifiers for correlating errors across logs and responses.
 */
@Component
public class TraceIdGenerator {

    /**
     * Generate a unique trace ID.
     * Uses UUID for simplicity, but can be replaced with more sophisticated
     * tracing systems like Zipkin, Jaeger, or OpenTelemetry.
     */
    public String generate() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * Generate a trace ID with a prefix.
     * Useful for categorizing different types of operations.
     */
    public String generate(String prefix) {
        return prefix + "-" + generate();
    }
}
