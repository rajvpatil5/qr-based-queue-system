package com.restaurant.waiting.model.waitEntry;

/**
 * Enum representing the status of a wait entry in the queue
 */
public enum WaitStatus {
    WAITING,    // Customer is waiting in queue
    NOTIFIED,   // Customer has been notified that table is available
    SEATED,     // Customer has been seated
    COMPLETED,   // Customer finished & left
    CANCELLED,  // Customer cancelled their wait entry
    SKIPPED     // Customer was notified but didn't respond (skipped)
}
