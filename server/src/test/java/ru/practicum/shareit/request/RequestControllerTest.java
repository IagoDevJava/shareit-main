package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithResponsesDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.model.RequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {
    @InjectMocks
    RequestController requestController;
    @Mock
    RequestService requestService;
    RequestDto requestDto;
    RequestWithResponsesDto requestWithResponsesDto;
    Request request;
    User user;
    LocalDateTime time;

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
                .created(time)
                .build();
        requestDto = RequestMapper.toRequestDto(request);
        requestWithResponsesDto = RequestMapper.toRequestWithResponsesDto(request);
    }

    @Test
    void addRequest() {
        when(requestService.addRequest(user.getId(), request)).thenReturn(requestDto);

        RequestDto actual = requestController.addRequest(user.getId(), request);

        assertEquals(requestDto, actual);
    }

    @Test
    void getYourRequestsWithData() {
        List<RequestWithResponsesDto> expected = List.of(requestWithResponsesDto);
        when(requestService.getYourRequestsWithData(user.getId())).thenReturn(expected);

        List<RequestWithResponsesDto> actual = requestController.getYourRequestsWithData(user.getId());

        assertEquals(expected, actual);
    }

    @Test
    void getAllRequests() {
        List<RequestWithResponsesDto> expected = List.of(requestWithResponsesDto);
        when(requestService.getAllRequests(user.getId(), 0L, 20L)).thenReturn(expected);

        List<RequestWithResponsesDto> actual = requestController.getAllRequests(user.getId(), 0L, 20L);

        assertEquals(expected, actual);
    }

    @Test
    void findRequestById() {
        RequestWithResponsesDto expected = RequestMapper.toRequestWithResponsesDto(request);
        when(requestService.findRequestById(user.getId(), request.getId())).thenReturn(expected);

        RequestWithResponsesDto actual = requestController.findRequestById(user.getId(), request.getId());

        assertEquals(expected, actual);
    }
}