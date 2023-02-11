package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemForResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithResponsesDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.model.RequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @InjectMocks
    RequestServiceImpl requestService;
    @Mock
    RequestRepository requestRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    RequestDto requestDto;
    RequestWithResponsesDto requestWithResponsesDto;
    Request request;
    User user;
    Item item;
    LocalDateTime time;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.now().withNano(0);
        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@email.ru")
                .build();
        item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .requestId(1L)
                .ownerId(user.getId())
                .available(true)
                .build();
        request = Request.builder()
                .id(1L)
                .description("description")
                .requesterID(user.getId())
                .created(time)
                .build();
        requestDto = RequestDto.builder()
                .id(1L)
                .description("description")
                .requesterId(user.getId())
                .created(request.getCreated())
                .build();
        requestWithResponsesDto = RequestMapper.toRequestWithResponsesDto(request);
    }

    @Test
    void addRequest() {
        when(requestRepository.save(request)).thenReturn(request);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        RequestDto actual = requestService.addRequest(user.getId(), request);

        assertEquals(requestDto, actual);
        verify(requestRepository).save(request);
    }

    @Test
    void addRequest_whenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> requestService.addRequest(2L, request));
        verify(requestRepository, never()).save(request);
    }

    @Test
    void getYourRequestsWithData() {
        requestWithResponsesDto = RequestMapper.toRequestWithResponsesDto(request);
        requestWithResponsesDto.setItems(new ArrayList<>());
        List<RequestWithResponsesDto> expected = List.of(requestWithResponsesDto);
        List<Request> requests = List.of(request);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(requestRepository.findRequestByRequesterID(user.getId())).thenReturn(requests);

        List<RequestWithResponsesDto> actual = requestService.getYourRequestsWithData(user.getId());

        assertEquals(expected, actual);
    }

    @Test
    void getAllRequests() {
        request.setRequesterID(2L);
        List<Request> requests = List.of(request);
        Page<Request> requestPage = new PageImpl<>(requests);
        List<RequestWithResponsesDto> expected = RequestMapper.mapToRequestWithResponseDto(requestPage);
        List<Item> itemsForRequest = List.of(item);
        List<ItemForResponseDto> itemDtos = ItemMapper.mapToItemForResponseDto(itemsForRequest);
        for (RequestWithResponsesDto withResponsesDto : expected) {
            withResponsesDto.setItems(itemDtos);
        }
        when(requestRepository.findAll(any(Pageable.class))).thenReturn(requestPage);
        when(itemRepository.findItemsByRequest(request.getDescription())).thenReturn(itemsForRequest);

        List<RequestWithResponsesDto> actual = requestService.getAllRequests(user.getId(), 0L, 20L);

        assertEquals(expected, actual);
    }

    @Test
    void findRequestById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(requestRepository.findById(request.getId())).thenReturn(Optional.ofNullable(request));
        requestWithResponsesDto.setItems(new ArrayList<>());

        RequestWithResponsesDto actual = requestService.findRequestById(user.getId(), request.getId());

        assertEquals(requestWithResponsesDto, actual);
    }

    @Test
    void findRequestById_thenReturnRequestNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        assertThrows(RequestNotFoundException.class, () -> requestService.findRequestById(user.getId(), request.getId()));
    }
}