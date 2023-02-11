package ru.practicum.shareit.exception;

public class RequestDescriptionIsNullException extends RuntimeException {
    public RequestDescriptionIsNullException(String message) {
        super(message);
    }
}
