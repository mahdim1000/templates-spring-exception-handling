package com.github.mahdim1000.exceptionhandling.exception;

import org.springframework.http.HttpStatus;

/**
 * Validation exceptions for input validation errors.
 * Represents client-side errors due to invalid input data.
 */
public class ValidationException extends BaseException {
    
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public ValidationException(String errorCode, String messageKey) {
        super(errorCode, messageKey);
    }

    public ValidationException(String errorCode, String messageKey, Object[] messageArgs) {
        super(errorCode, messageKey, messageArgs);
    }

    public ValidationException(String errorCode, String messageKey, Throwable cause) {
        super(errorCode, messageKey, cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
