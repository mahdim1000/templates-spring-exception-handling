package com.github.mahdim1000.exceptionhandling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    String errorCode,
    String message,
    String path,
    int status,
    LocalDateTime timestamp,
    List<FieldError> fieldErrors,
    String traceId
) {

    public ErrorResponse {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    /**
     * Constructor without fieldErrors for non-validation errors
     */
    public ErrorResponse(String errorCode, String message, String path, int status, String traceId) {
        this(errorCode, message, path, status, LocalDateTime.now(), null, traceId);
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private String errorCode;
        private String message;
        private String path;
        private int status;
        private LocalDateTime timestamp;
        private List<FieldError> fieldErrors;
        private String traceId;

        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder fieldErrors(List<FieldError> fieldErrors) {
            this.fieldErrors = fieldErrors;
            return this;
        }

        public Builder traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(errorCode, message, path, status, timestamp, fieldErrors, traceId);
        }
    }
}
