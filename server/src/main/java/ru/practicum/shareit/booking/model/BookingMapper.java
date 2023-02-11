package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    /**
     * Конвертация Booking в BookingDto
     */
    public static BookingDto toBookingDto(Booking booking, Item item, User user) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(item)
                .booker(UserMapper.toUserDto(user))
                .status(booking.getStatus())
                .build();
    }

    /**
     * Конвертация Booking в BookingDtoItem
     */
    public static BookingDtoItem toBookingDtoItem(Booking booking) {
        return BookingDtoItem.builder()
                .id(booking.getId())
                .bookerId(booking.getBookerId())
                .build();
    }

    /**
     * Конвертация списка Booking в BookingDtoItem
     */
    public static List<BookingDto> mapToBookingDto(Iterable<Booking> bookings, Item item, User user) {
        List<BookingDto> result = new ArrayList<>();

        for (Booking booking : bookings) {
            result.add(toBookingDto(booking, item, user));
        }

        return result;
    }
}