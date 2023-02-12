package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;

@Validated
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Добавление нового бронирования.
     */
    @PostMapping
    public BookingDto addNewBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @Valid @RequestBody Booking booking) {

        return bookingService.addNewBooking(userId, booking);
    }

    /**
     * Подтверждение или отклонение запроса на бронирование.
     */
    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long bookingId,
                                    @RequestParam String approved) {
        log.info("Отправляем запрос на бронирование");
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус)
     */
    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId) {
        log.info("Отправляем запрос о данных бронирования {}", bookingId);
        return bookingService.getBookingById(userId, bookingId);
    }

    /**
     * Получение списка всех бронирований текущего пользователя
     */
    @GetMapping()
    public List<BookingDto> getBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(required = false) String state,
                                                  @RequestParam(defaultValue = "0") @Min(0) Long from,
                                                  @RequestParam(defaultValue = "20") @Min(1) Long size) {
        log.info("Отправляем запрос на получение бронирования пользователя {}", userId);
        return bookingService.getBookingsByBookerId(
                userId, Objects.requireNonNullElse(state, "ALL"), from, size);
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя
     */
    @GetMapping("/owner")
    public List<BookingDto> getBookingAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                        @RequestParam(required = false) String state,
                                                        @RequestParam(defaultValue = "0") @Min(0) Long from,
                                                        @RequestParam(defaultValue = "20") @Min(1) Long size) {
        log.info("Отправляем запрос на получение списка бронирований для всех вещей пользователя {}", ownerId);
        return bookingService.getBookingAllItemsByOwnerId(
                ownerId, Objects.requireNonNullElse(state, "ALL"), from, size);
    }
}
