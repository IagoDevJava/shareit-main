package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithResponsesDto;
import ru.practicum.shareit.request.model.Request;

import javax.validation.constraints.Min;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }


    /**
     * добавить новый запрос вещи.
     */
    @PostMapping()
    public RequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody Request request) {
        log.info("Делаем запрос {}", request.getDescription());
        return requestService.addRequest(userId, request);
    }

    /**
     * получить список своих запросов вместе с данными об ответах на них.
     */
    @GetMapping()
    public List<RequestWithResponsesDto> getYourRequestsWithData(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пользователь получает список своих запросов вместе с данными об ответах на них");
        return requestService.getYourRequestsWithData(userId);
    }

    /**
     * получить список запросов, созданных другими пользователями
     */
    @GetMapping("/all")
    public List<RequestWithResponsesDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(defaultValue = "0") @Min(0) Long from,
                                                        @RequestParam(defaultValue = "20") @Min(1) Long size) {
        log.info("Получаем список запросов, созданных другими пользователями");
        return requestService.getAllRequests(userId, from, size);
    }

    /**
     * получить данные об одном конкретном запросе вместе с данными об ответах на него
     */
    @GetMapping("/{requestId}")
    public RequestWithResponsesDto findRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @PathVariable Long requestId) {
        log.info("ПОлучаем данные об одном конкретном запросе вместе с данными об ответах");
        return requestService.findRequestById(userId, requestId);
    }
}
