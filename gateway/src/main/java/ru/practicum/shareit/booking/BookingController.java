package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.StateNotCurrentException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    /**
     * Добавление нового бронирования.
     */
    @PostMapping
    public ResponseEntity<Object> bookItem(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    /**
     * Подтверждение или отклонение запроса на бронирование.
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setApproved(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PositiveOrZero @PathVariable Long bookingId,
                                              @RequestParam String approved) {
        log.info("Set approved for booking {}", bookingId);
        return bookingClient.setApproved(userId, bookingId, approved);
    }

    /**
     * Получение списка всех бронирований текущего пользователя
     */
    @GetMapping
    public ResponseEntity<Object> getBookings(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new StateNotCurrentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус)
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId,
                                             @PositiveOrZero @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwnerId(
            @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new StateNotCurrentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookingByOwnerId(userId, state, from, size);
    }
}
