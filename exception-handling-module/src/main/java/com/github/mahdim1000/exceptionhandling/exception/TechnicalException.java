package com.github.mahdim1000.exceptionhandling.exception;

import org.springframework.http.HttpStatus;

/**
 * Technical exceptions that represent system-level errors.
 * These are typically infrastructure-related issues that should not expose
 * technical details to end users.
 */
public class TechnicalException extends BaseException {
    
    private final HttpStatus httpStatus;

    public TechnicalException(String errorCode, String messageKey) {
        this(errorCode, messageKey, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public TechnicalException(String errorCode, String messageKey, Throwable cause) {
        this(errorCode, messageKey, HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    public TechnicalException(String errorCode, String messageKey, HttpStatus httpStatus) {
        super(errorCode, messageKey);
        this.httpStatus = httpStatus;
    }

    public TechnicalException(String errorCode, String messageKey, HttpStatus httpStatus, Throwable cause) {
        super(errorCode, messageKey, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
