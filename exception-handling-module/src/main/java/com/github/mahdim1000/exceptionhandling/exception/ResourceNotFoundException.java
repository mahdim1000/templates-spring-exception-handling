package com.github.mahdim1000.exceptionhandling.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for resource not found scenarios.
 * Represents HTTP 404 situations in a type-safe manner.
 */
public class ResourceNotFoundException extends BaseException {
    
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException(String errorCode, String messageKey) {
        super(errorCode, messageKey);
    }

    public ResourceNotFoundException(String errorCode, String messageKey, Object[] messageArgs) {
        super(errorCode, messageKey, messageArgs);
    }

    public ResourceNotFoundException(String errorCode, String messageKey, Throwable cause) {
        super(errorCode, messageKey, cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
