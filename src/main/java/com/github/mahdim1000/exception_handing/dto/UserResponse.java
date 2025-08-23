package com.github.mahdim1000.exception_handing.dto;

/**
 * DTO for user response.
 * Simple response object for demonstration purposes.
 * 
 * Uses Java record for immutability and conciseness.
 */
public record UserResponse(
    Long id,
    String name,
    String email,
    Integer age,
    String phone,
    String status
) {
    // Compact constructor for validation
    public UserResponse {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
    }
}
