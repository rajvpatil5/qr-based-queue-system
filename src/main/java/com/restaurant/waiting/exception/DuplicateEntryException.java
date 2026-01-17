package com.restaurant.waiting.exception;

/**
 * Exception thrown when a user tries to join the queue with a mobile number
 * that already has an active waiting entry
 */
public class DuplicateEntryException extends RuntimeException {

    public DuplicateEntryException(String message) {
        super(message);
    }

    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}
