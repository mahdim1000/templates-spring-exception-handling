package com.github.mahdim1000.exception_handing.controller;

import com.github.mahdim1000.exception_handing.dto.UserRequest;
import com.github.mahdim1000.exception_handing.dto.UserResponse;
import com.github.mahdim1000.exceptionhandling.exception.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/demo")
public class DemoController {

    /**
     * Simulate database connection error - demonstrates TechnicalException.
     */
    @GetMapping("/simulate/database-error")
    public ResponseEntity<String> simulateDatabaseError() {
        throw new TechnicalException(
            "DATABASE_CONNECTION_FAILED",
            "error.technical.database.connection",
            new RuntimeException("Connection timeout")
        );
    }

    /**
     * Simulate external service error - demonstrates TechnicalException.
     */
    @GetMapping("/simulate/service-error")
    public ResponseEntity<String> simulateServiceError() {
        throw new TechnicalException(
            "EXTERNAL_SERVICE_UNAVAILABLE",
            "error.technical.external.service"
        );
    }

    /**
     * Simulate validation error - demonstrates ValidationException.
     */
    @PostMapping("/simulate/validation-error")
    public ResponseEntity<String> simulateValidationError(@RequestParam String email) {
        
        if (email == null || !email.contains("@")) {
            throw new ValidationException(
                "INVALID_EMAIL_FORMAT",
                "error.validation.email.invalid"
            );
        }
        
        return ResponseEntity.ok("Email is valid");
    }

    /**
     * Simulate unexpected error - demonstrates generic exception handling.
     */
    @GetMapping("/simulate/unexpected-error")
    public ResponseEntity<String> simulateUnexpectedError() {
        // This will be caught by the generic exception handler
        throw new RuntimeException("Something unexpected happened!");
    }

    /**
     * Demonstrate Bean Validation with field errors.
     */
    @PostMapping("/validate")
    public ResponseEntity<UserResponse> validateUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse response = new UserResponse(
            1L, 
            userRequest.getName(), 
            userRequest.getEmail(), 
            userRequest.getAge(),
            userRequest.getPhone(),
            "ACTIVE"
        );
        return ResponseEntity.ok(response);
    }

}
