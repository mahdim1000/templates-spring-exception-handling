package com.github.mahdim1000.exceptionhandling.exception;

/**
 * Base exception class for all custom exceptions in the application.
 *
 * This class provides:
 * - Consistent error code structure
 * - I18n message key support
 * - Additional context parameters for dynamic messages
 */
public abstract class BaseException extends RuntimeException {
    
    private final String errorCode;
    private final String messageKey;
    private final Object[] messageArgs;

    protected BaseException(String errorCode, String messageKey) {
        this(errorCode, messageKey, null, null);
    }

    protected BaseException(String errorCode, String messageKey, Object[] messageArgs) {
        this(errorCode, messageKey, messageArgs, null);
    }

    protected BaseException(String errorCode, String messageKey, Throwable cause) {
        this(errorCode, messageKey, null, cause);
    }

    protected BaseException(String errorCode, String messageKey, Object[] messageArgs, Throwable cause) {
        super(messageKey, cause);
        this.errorCode = errorCode;
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }
}
