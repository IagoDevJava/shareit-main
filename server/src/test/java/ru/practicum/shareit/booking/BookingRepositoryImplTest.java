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
import ru.practicum.shareit.exception.BookingChangeApprovedStatusException;
import ru.practicum.shareit.exception.OwnerNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingRepositoryImplTest {
    @InjectMocks
    BookingRepositoryImpl bookingRepositoryImpl;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;

    Booking booking;
    BookingDto expected;
    User booker;
    User owner;
    Item item;
    LocalDateTime time;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.now();
        owner = User.builder()
                .id(2L)
                .name("owner")
                .email("owner@email.ru")
                .build();
        booker = User.builder()
                .id(0L)
                .name("booker")
                .email("booker@email.ru")
                .build();
        item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .requestId(1L)
                .ownerId(owner.getId())
                .available(true)
                .build();
        booking = Booking.builder()
                .id(1L)
                .status(Status.WAITING)
                .bookerId(booker.getId())
                .itemId(item.getId())
                .start(time.plusMinutes(20L))
                .end(time.plusMinutes(40L))
                .build();
        expected = BookingMapper.toBookingDto(booking, item, owner);
    }

    @Test
    void update() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.ofNullable(booking));
        when(userRepository.findById(booking.getBookerId())).thenReturn(Optional.ofNullable(booker));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.ofNullable(item));

        BookingDto actual = bookingRepositoryImpl.update(owner.getId(), booking.getId(), "true");

        assertEquals(expected, actual);
        verify(bookingRepository).save(booking);
    }

    @Test
    void update_thenReturnOwnerNotFoundException() {
        item.setOwnerId(33L);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.ofNullable(booking));
        when(userRepository.findById(booking.getBookerId())).thenReturn(Optional.ofNullable(booker));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.ofNullable(item));

        assertThrows(OwnerNotFoundException.class,
                () -> bookingRepositoryImpl.update(owner.getId(), booking.getId(), "true"));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void update_thenReturnBookingChangeApprovedStatusException() {
        booking.setStatus(Status.APPROVED);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.ofNullable(booking));

        assertThrows(BookingChangeApprovedStatusException.class,
                () -> bookingRepositoryImpl.update(owner.getId(), booking.getId(), "true"));
        verify(bookingRepository, never()).save(booking);
    }
}