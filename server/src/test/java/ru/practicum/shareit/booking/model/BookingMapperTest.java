package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    private Booking booking;
    private BookingDto expected;
    private Item item;
    private User user;
    LocalDateTime time;
    private BookingDtoItem expectedBookingDtoItem;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.now();
        item = Item.builder()
                .id(0L)
                .name("name")
                .description("description")
                .requestId(1L)
                .ownerId(2L)
                .available(true)
                .build();
        user = User.builder()
                .id(2L)
                .name("name")
                .email("name@email.ru")
                .build();
        booking = Booking.builder()
                .id(1L)
                .status(Status.APPROVED)
                .bookerId(2L)
                .itemId(0L)
                .start(time.minusHours(1L))
                .end(time.minusMinutes(1L))
                .build();
        expected = BookingDto.builder()
                .id(1L)
                .status(Status.APPROVED)
                .booker(UserMapper.toUserDto(user))
                .item(item)
                .start(time.minusHours(1L))
                .end(time.minusMinutes(1L))
                .build();
        expectedBookingDtoItem = BookingDtoItem.builder()
                .id(1L)
                .bookerId(2L)
                .build();
    }

    @Test
    void toBookingDto() {
        BookingDto actual = BookingMapper.toBookingDto(booking, item, user);

        assertEquals(expected, actual);
    }

    @Test
    void toBookingDtoItem() {
        BookingDtoItem bookingDtoItem = BookingMapper.toBookingDtoItem(booking);

        assertEquals(expectedBookingDtoItem, bookingDtoItem);
    }
}