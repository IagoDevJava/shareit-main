package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithResponsesDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestMapperTest {
    Request request;
    private LocalDateTime time;
    private User user;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.now();
        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@email.ru")
                .build();
        request = Request.builder()
                .id(1L)
                .description("description")
                .requesterID(user.getId())
                .created(time.plusMinutes(5))
                .build();
    }

    @Test
    void toRequestDto() {
        RequestDto expected = RequestDto.builder()
                .id(1L)
                .description("description")
                .requesterId(user.getId())
                .created(time.plusMinutes(5))
                .build();

        RequestDto actual = RequestMapper.toRequestDto(request);

        assertEquals(expected, actual);
    }

    @Test
    void toRequestWithResponsesDto() {
        RequestWithResponsesDto expected = RequestWithResponsesDto.builder()
                .id(1L)
                .description("description")
                .requesterId(user.getId())
                .created(time.plusMinutes(5))
                .build();

        RequestWithResponsesDto actual = RequestMapper.toRequestWithResponsesDto(request);

        assertEquals(expected, actual);
    }
}