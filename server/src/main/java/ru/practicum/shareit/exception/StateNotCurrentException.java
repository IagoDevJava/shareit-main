package ru.practicum.shareit.exception;

public class StateNotCurrentException extends RuntimeException {
    public StateNotCurrentException(String message) {
        super(message);
    }
}
