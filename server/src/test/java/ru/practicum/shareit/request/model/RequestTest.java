package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.RequestDescriptionIsNullException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestTest {
    Request request;
    LocalDateTime time;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.now().withNano(0);
        request = Request.builder()
                .id(1L)
                .description("description")
                .requesterID(1L)
                .created(time)
                .build();
    }

    @Test
    void getDescriptionIsOk() {
        String expected = "description";

        String actual = request.getDescription();

        assertEquals(expected, actual);
    }

    @Test
    void getNullAndRequestDescriptionIsNullException() {
        request.setDescription(null);

        assertThrows(RequestDescriptionIsNullException.class, () -> request.getDescription());
    }
}