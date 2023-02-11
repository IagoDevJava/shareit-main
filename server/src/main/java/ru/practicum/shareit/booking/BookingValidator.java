package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BookingCreateOwnerException;
import ru.practicum.shareit.exception.BookingStartDateTimeException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Slf4j
public class BookingValidator {
    /**
     * Валидация бронирования при создании
     */
    public static void isValidBooking(Booking booking, User user, Item item) {
        if (user.getId().equals(item.getOwnerId())) {
            throw new BookingCreateOwnerException("Владелец вещи не может ее забронировать");
        }
        if (booking.getStart().isAfter(booking.getEnd())
                || booking.getEnd().isBefore(LocalDateTime.now())
                || booking.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingStartDateTimeException("Даты бронирования не верны");
        }
    }
}