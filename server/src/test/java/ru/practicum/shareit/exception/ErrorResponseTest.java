package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void getError() {
        String expected = "error";
        ErrorResponse errorResponse = new ErrorResponse("error");

        String actual = errorResponse.getError();

        assertEquals(expected, actual);
    }
}