package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithResponsesDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

@Service
public interface RequestService {
    /**
     * добавить новый запрос вещи
     */
    RequestDto addRequest(Long userId, Request request);

    /**
     * получить список своих запросов вместе с данными об ответах на них
     */
    List<RequestWithResponsesDto> getYourRequestsWithData(Long userId);

    /**
     * получить список запросов, созданных другими пользователями
     */
    List<RequestWithResponsesDto> getAllRequests(Long userId, Long from, Long size);

    /**
     * получить данные об одном конкретном запросе вместе с данными об ответах на него
     */
    RequestWithResponsesDto findRequestById(Long userId, Long requestId);
}
