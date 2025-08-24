package com.github.mahdim1000.exceptionhandling.handler;

import com.github.mahdim1000.exceptionhandling.dto.ErrorResponse;
import com.github.mahdim1000.exceptionhandling.dto.FieldError;
import com.github.mahdim1000.exceptionhandling.exception.*;
import com.github.mahdim1000.exceptionhandling.util.MessageResolver;
import com.github.mahdim1000.exceptionhandling.util.TraceIdGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * This handler provides:
 * - Consistent error response structure
 * - Internationalized error messages
 * - Proper HTTP status codes
 * - Trace ID generation for error tracking
 * - Separation between business and technical errors
 * 
 * Follows the Open-Closed Principle - open for extension, closed for modification.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private final MessageResolver messageResolver;
    private final TraceIdGenerator traceIdGenerator;

    public GlobalExceptionHandler(MessageResolver messageResolver, TraceIdGenerator traceIdGenerator) {
        this.messageResolver = messageResolver;
        this.traceIdGenerator = traceIdGenerator;
    }

    /**
     * Handle custom business exceptions.
     * These are expected errors that should be shown to users.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        String traceId = traceIdGenerator.generate("BIZ");
        String message = messageResolver.resolve(ex.getMessageKey(), ex.getMessageArgs());
        
        logger.warn("Business exception [{}]: {} - {}", traceId, ex.getErrorCode(), message, ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .message(message)
                .status(ex.getHttpStatus().value())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    /**
     * Handle custom technical exceptions.
     * These are system-level errors that shouldn't expose technical details.
     */
    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<ErrorResponse> handleTechnicalException(
            TechnicalException ex, HttpServletRequest request) {
        
        String traceId = traceIdGenerator.generate("TECH");
        String message = messageResolver.resolve(ex.getMessageKey(), ex.getMessageArgs());
        
        logger.error("Technical exception [{}]: {} - {}", traceId, ex.getErrorCode(), message, ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .message(message)
                .status(ex.getHttpStatus().value())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    /**
     * Handle validation exceptions.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex, HttpServletRequest request) {
        
        String traceId = traceIdGenerator.generate("VAL");
        String message = messageResolver.resolve(ex.getMessageKey(), ex.getMessageArgs());
        
        logger.warn("Validation exception [{}]: {} - {}", traceId, ex.getErrorCode(), message);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .message(message)
                .status(ex.getHttpStatus().value())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    /**
     * Handle resource not found exceptions.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        
        String traceId = traceIdGenerator.generate("NF");
        String message = messageResolver.resolve(ex.getMessageKey(), ex.getMessageArgs());
        
        logger.warn("Resource not found [{}]: {} - {}", traceId, ex.getErrorCode(), message);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .message(message)
                .status(ex.getHttpStatus().value())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    /**
     * Handle Spring validation errors (Bean Validation).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        String traceId = traceIdGenerator.generate("VALID");
        
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldError(
                        messageResolver.resolve("field." + error.getField(), error.getField()),
                        error.getRejectedValue(),
                        messageResolver.resolve(error.getDefaultMessage(), error.getArguments(), error.getDefaultMessage())
                ))
                .collect(Collectors.toList());

        logger.warn("Validation failed [{}]: {} field errors", traceId, fieldErrors.size());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("VALIDATION_FAILED")
                .message(messageResolver.resolve("error.validation.failed", "Validation failed"))
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .traceId(traceId)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle binding exceptions.
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(
            BindException ex, HttpServletRequest request) {
        
        String traceId = traceIdGenerator.generate("BIND");
        
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldError(
                        messageResolver.resolve("field." + error.getField(), error.getField()),
                        error.getRejectedValue(),
                        messageResolver.resolve(error.getDefaultMessage(), error.getDefaultMessage())
                ))
                .collect(Collectors.toList());

        logger.warn("Binding failed [{}]: {} field errors", traceId, fieldErrors.size());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("BINDING_FAILED")
                .message(messageResolver.resolve("error.validation.binding", "Binding validation failed"))
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .traceId(traceId)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle HTTP method not supported.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        
        String traceId = traceIdGenerator.generate("METHOD");
        String message = messageResolver.resolve("http.405", "Method Not Allowed");
        
        logger.warn("Method not supported [{}]: {} for {}", traceId, ex.getMethod(), request.getRequestURI());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("METHOD_NOT_ALLOWED")
                .message(message)
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    /**
     * Handle missing request parameters.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        String traceId = traceIdGenerator.generate("PARAM");
        String message = messageResolver.resolve("error.validation.required", 
                new Object[]{ex.getParameterName()});
        
        logger.warn("Missing parameter [{}]: {}", traceId, ex.getParameterName());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("MISSING_PARAMETER")
                .message(message)
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle method argument type mismatch.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        String traceId = traceIdGenerator.generate("TYPE");
        String message = messageResolver.resolve("error.validation.type.mismatch", 
                new Object[]{ex.getName(), ex.getRequiredType().getSimpleName()});
        
        logger.warn("Type mismatch [{}]: {} expected {}", traceId, ex.getName(), ex.getRequiredType().getSimpleName());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("TYPE_MISMATCH")
                .message(message)
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle malformed JSON requests.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        String traceId = traceIdGenerator.generate("JSON");
        String message = messageResolver.resolve("error.validation.json.malformed", "Malformed JSON request");
        
        logger.warn("Malformed JSON [{}]: {}", traceId, ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("MALFORMED_JSON")
                .message(message)
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle no handler found (404).
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request) {
        
        String traceId = traceIdGenerator.generate("404");
        String message = messageResolver.resolve("http.404", "Not Found");
        
        logger.warn("No handler found [{}]: {} {}", traceId, ex.getHttpMethod(), ex.getRequestURL());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("NOT_FOUND")
                .message(message)
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /* 
     * Note: DataIntegrityViolationException handler can be added when using database dependencies
     * 
     * @ExceptionHandler(DataIntegrityViolationException.class)
     * public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
     *         DataIntegrityViolationException ex, HttpServletRequest request) {
     *     // Implementation here
     * }
     */

    /**
     * Handle all other unexpected exceptions.
     * This is the fallback handler for any unhandled exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        String traceId = traceIdGenerator.generate("ERR");
        String message = messageResolver.resolve("error.general.internal", "An internal server error occurred");
        
        logger.error("Unexpected error [{}]: {}", traceId, ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("INTERNAL_SERVER_ERROR")
                .message(message)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
