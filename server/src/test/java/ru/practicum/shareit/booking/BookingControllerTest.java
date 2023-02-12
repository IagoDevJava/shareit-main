package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;
    Item item;
    User user;
    Booking booking;
    Comment comment;
    LocalDateTime time;
    BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.now();
        item = Item.builder()
                .id(1L)
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
                .status(Status.WAITING)
                .bookerId(2L)
                .itemId(1L)
                .start(time.minusHours(1L))
                .end(time.minusMinutes(1L))
                .build();
        comment = Comment.builder()
                .id(1L)
                .text("comment")
                .itemId(1L)
                .authorId(2L)
                .created(time)
                .build();
        bookingDto = BookingMapper.toBookingDto(booking, item, user);
    }

    @Test
    void addNewBooking() {
        when(bookingService.addNewBooking(user.getId(), booking))
                .thenReturn(bookingDto);

        BookingDto actualBooking = bookingController.addNewBooking(user.getId(), booking);

        assertEquals(bookingDto, actualBooking);
    }

    @Test
    void updateBooking() {
        when(bookingService.updateBooking(user.getId(), booking.getId(), "REJECTED"))
                .thenReturn(bookingDto);

        BookingDto actualBooking = bookingController.updateBooking(user.getId(), booking.getId(), "REJECTED");

        assertEquals(bookingDto, actualBooking);
    }

    @Test
    void getBookingById() {
        when(bookingService.getBookingById(user.getId(), booking.getId())).thenReturn(bookingDto);

        BookingDto actualBookingDto = bookingController.getBookingById(user.getId(), booking.getId());

        assertEquals(bookingDto, actualBookingDto);
    }

    @Test
    void getBookingsByBookerId() {
        List<BookingDto> expected = List.of(bookingDto);
        when(bookingService.getBookingsByBookerId(user.getId(), "ALL", null, null))
                .thenReturn(expected);

        List<BookingDto> actualBookingsByBookerId =
                bookingController.getBookingsByBookerId(user.getId(), "ALL", null, null);

        assertEquals(expected, actualBookingsByBookerId);
    }

    @Test
    void getBookingAllItemsByOwnerId() {
        List<BookingDto> expected = List.of(bookingDto);
        when(bookingService.getBookingAllItemsByOwnerId(user.getId(), "ALL", null, null))
                .thenReturn(expected);

        List<BookingDto> actualBookingsByBookerId =
                bookingController.getBookingAllItemsByOwnerId(user.getId(), "ALL", null, null);

        assertEquals(expected, actualBookingsByBookerId);
    }
}