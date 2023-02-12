package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.RequestRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    /**
     * добавить новый запрос вещи.
     */
    @PostMapping()
    public ResponseEntity<Object> saveRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @Valid @RequestBody RequestRequestDto requestRequestDto) {
        log.info("Save request {}", requestRequestDto.getDescription());
        return requestClient.saveRequest(userId, requestRequestDto);
    }

    /**
     * получить список своих запросов вместе с данными об ответах на них.
     */
    @GetMapping()
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get requests for user {}", userId);
        return requestClient.getRequests(userId);
    }

    /**
     * получить список запросов, созданных другими пользователями
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsWithAll(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get request with userId={}, from={}, size={}", userId, from, size);
        return requestClient.getAllRequests(userId, from, size);
    }

    /**
     * получить данные об одном конкретном запросе вместе с данными об ответах на него
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long requestId) {
        log.info("Get request with userId={}, requestId={}", userId, requestId);
        return requestClient.getRequest(userId, requestId);
    }
}
