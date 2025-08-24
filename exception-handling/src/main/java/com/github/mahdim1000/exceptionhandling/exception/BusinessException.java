package com.github.mahdim1000.exceptionhandling.exception;

import org.springframework.http.HttpStatus;

/**
 * Business logic exceptions that represent expected error conditions.
 * These exceptions should be handled gracefully and presented to users.
 */
public class BusinessException extends BaseException {
    
    private final HttpStatus httpStatus;

    public BusinessException(String errorCode, String messageKey) {
        this(errorCode, messageKey, HttpStatus.BAD_REQUEST);
    }

    public BusinessException(String errorCode, String messageKey, Object[] messageArgs) {
        this(errorCode, messageKey, messageArgs, HttpStatus.BAD_REQUEST);
    }

    public BusinessException(String errorCode, String messageKey, HttpStatus httpStatus) {
        super(errorCode, messageKey);
        this.httpStatus = httpStatus;
    }

    public BusinessException(String errorCode, String messageKey, Object[] messageArgs, HttpStatus httpStatus) {
        super(errorCode, messageKey, messageArgs);
        this.httpStatus = httpStatus;
    }

    public BusinessException(String errorCode, String messageKey, Throwable cause) {
        super(errorCode, messageKey, cause);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
