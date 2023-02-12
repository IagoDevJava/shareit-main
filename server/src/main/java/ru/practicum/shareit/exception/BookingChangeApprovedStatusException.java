package ru.practicum.shareit.exception;

public class BookingChangeApprovedStatusException extends RuntimeException {
    public BookingChangeApprovedStatusException(String message) {
        super(message);
    }
}
