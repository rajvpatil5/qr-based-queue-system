package com.restaurant.waiting.exception;

import lombok.Data;

import java.time.Instant;

/**
 * DTO for error responses
 */
@Data
public class ErrorResponse {

    private Instant timestamp = Instant.now();
    private int status;
    private String error;
    private String message;
    private String path;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
