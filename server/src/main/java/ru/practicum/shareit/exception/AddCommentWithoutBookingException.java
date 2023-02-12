package ru.practicum.shareit.exception;

public class AddCommentWithoutBookingException extends RuntimeException {
    public AddCommentWithoutBookingException(String message) {
        super(message);
    }
}
