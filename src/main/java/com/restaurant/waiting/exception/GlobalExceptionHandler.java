package com.restaurant.waiting.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST controllers
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String message = "Validation failed: " + errors.toString();
        logger.warn("Validation error: {}", message);

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            message,
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle duplicate entry exceptions
     */
    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEntryException(
            DuplicateEntryException ex, WebRequest request) {
        
        logger.warn("Duplicate entry error: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Duplicate Entry",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Handle resource not found exceptions
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        logger.warn("Resource not found: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle invalid status exceptions
     */
    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStatusException(
            InvalidStatusException ex, WebRequest request) {
        
        logger.warn("Invalid status error: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Status",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle illegal state exceptions (e.g., invalid status transitions)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(
            IllegalStateException ex, WebRequest request) {
        
        logger.warn("Illegal state error: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Operation",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        
        logger.error("Unexpected error occurred", ex);

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred. Please try again later.",
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
