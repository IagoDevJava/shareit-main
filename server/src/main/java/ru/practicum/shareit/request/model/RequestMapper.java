package ru.practicum.shareit.request.model;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithResponsesDto;

import java.util.ArrayList;
import java.util.List;

public class RequestMapper {
    /**
     * Конвертирование Request в RequestDto
     */
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requesterId(request.getRequesterID() != null ? request.getRequesterID() : null)
                .created(request.getCreated())
                .build();
    }

    /**
     * Конвертирование Request в RequestWithResponsesDto
     */
    public static RequestWithResponsesDto toRequestWithResponsesDto(Request request) {
        return RequestWithResponsesDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requesterId(request.getRequesterID() != null ? request.getRequesterID() : null)
                .created(request.getCreated())
                .build();
    }

    /**
     * Конвертация списка Request в RequestWithResponsesDto
     */
    public static List<RequestWithResponsesDto> mapToRequestWithResponseDto(Iterable<Request> requests) {
        List<RequestWithResponsesDto> result = new ArrayList<>();

        for (Request request : requests) {
            result.add(toRequestWithResponsesDto(request));
        }

        return result;
    }
}
