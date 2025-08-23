package com.github.mahdim1000.exceptionhandling.dto;

public record FieldError(
    String field,
    Object rejectedValue,
    String message
) {
    public FieldError {
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException("Field name cannot be null or blank");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Error message cannot be null or blank");
        }
    }
}
