package com.restaurant.waiting.exception;

/**
 * Exception thrown when an invalid status transition is attempted
 */
public class InvalidStatusException extends RuntimeException {

    public InvalidStatusException(String message) {
        super(message);
    }

    public InvalidStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
