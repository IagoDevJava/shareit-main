package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @InjectMocks
    BookingServiceImpl bookingService;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    BookingRepositoryImpl bookingRepositoryImpl;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;

    Booking booking;
    User owner;
    User user;
    User booker;
    Item item;
    LocalDateTime time;
    BookingDto expected;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.now();
        owner = User.builder()
                .id(2L)
                .name("owner")
                .email("owner@email.ru")
                .build();
        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@email.ru")
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
    void addNewBooking() {
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));

        BookingDto actualBookingDto = bookingService.addNewBooking(user.getId(), booking);

        assertEquals(expected, actualBookingDto);
        verify(bookingRepository).save(booking);
    }

    @Test
    void addNewBooking_whenUserIsOwner_thenBookingCreateOwnerException() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));

        assertThrows(BookingCreateOwnerException.class,
                () -> bookingService.addNewBooking(owner.getId(), booking));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void addNewBooking_whenStartAfterEnd_thenBookingStartDateTimeException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        booking.setStart(LocalDateTime.now().plusMinutes(40L));
        booking.setEnd(LocalDateTime.now().plusMinutes(20L));

        assertThrows(BookingStartDateTimeException.class,
                () -> bookingService.addNewBooking(user.getId(), booking));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void addNewBooking_whenEndBeforeNow_thenBookingStartDateTimeException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        booking.setEnd(LocalDateTime.now().minusMinutes(20L));

        assertThrows(BookingStartDateTimeException.class,
                () -> bookingService.addNewBooking(user.getId(), booking));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void addNewBooking_whenItemAvailableIsFalse_thenItemAvailabilityException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        item.setAvailable(false);


        assertThrows(ItemAvailabilityException.class,
                () -> bookingService.addNewBooking(user.getId(), booking));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void addNewBooking_whenStartBeforeNow_thenBookingStartDateTimeException() {
        item.setAvailable(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));

        assertThrows(ItemAvailabilityException.class,
                () -> bookingService.addNewBooking(user.getId(), booking));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void getBookingById_whenUserIsBooker() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.ofNullable(booking));
        when(userRepository.findById(booking.getBookerId())).thenReturn(Optional.ofNullable(booker));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));

        BookingDto actualBookingById = bookingService.getBookingById(booker.getId(), booking.getId());

        assertEquals(expected, actualBookingById);
    }

    @Test
    void getBookingById_whenUserIsOwner() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.ofNullable(booking));
        when(userRepository.findById(booking.getBookerId())).thenReturn(Optional.ofNullable(booker));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));

        BookingDto actualBookingById = bookingService.getBookingById(owner.getId(), booking.getId());

        assertEquals(expected, actualBookingById);
    }

    @Test
    void getBookingById_whenUserNotOwnerAndBooker_thenUserNotFoundException() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.ofNullable(booking));
        when(userRepository.findById(booking.getBookerId())).thenReturn(Optional.ofNullable(booker));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));

        assertThrows(UserNotFoundException.class,
                () -> bookingService.getBookingById(user.getId(), booking.getId()));
    }

    @Test
    void getBookingById_whenItemNotFound_thenItemNotFoundException() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.ofNullable(booking));
        when(userRepository.findById(booking.getBookerId())).thenReturn(Optional.ofNullable(booker));

        assertThrows(ItemNotFoundException.class,
                () -> bookingService.getBookingById(booker.getId(), booking.getId()));
    }

    @Test
    void getBookingById_whenBookingNotFound_thenBookingNotFoundException() {

        assertThrows(BookingNotFoundException.class,
                () -> bookingService.getBookingById(booker.getId(), booking.getId()));
    }

    @Test
    void getBookingById_whenUserNotBookerOrOwner_thenUserNotFoundException() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.ofNullable(booking));

        assertThrows(UserNotFoundException.class,
                () -> bookingService.getBookingById(booker.getId(), booking.getId()));
    }

    @Test
    void getBookingsByBookerId() {
        List<Booking> bookings = List.of(booking);
        Page<Booking> expectedList = new PageImpl<>(bookings);
        when(userRepository.findById(booking.getBookerId())).thenReturn(Optional.ofNullable(booker));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findAll(any(Pageable.class))).thenReturn(expectedList);

        List<BookingDto> actualBookingsByBookerId =
                bookingService.getBookingsByBookerId(booker.getId(), "ALL", 0L, 2L);

        assertEquals(BookingMapper.mapToBookingDto(bookings, item, user), actualBookingsByBookerId);
    }

    @Test
    void getBookingsByBookerId_whenUserNotFound_thenUserNotFoundException() {
        assertThrows(UserNotFoundException.class,
                () -> bookingService.getBookingsByBookerId(booker.getId(), null, null, null));
    }

    @Test
    void getBookingAllItemsByOwnerId() {
        List<Booking> bookings = List.of(booking);
        List<Item> items = List.of(item);
        when(bookingRepository.findBookingByItemId(item.getId())).thenReturn(bookings);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findItemByOwnerId(owner.getId())).thenReturn(items);
        when(userRepository.findById(booking.getBookerId())).thenReturn(Optional.ofNullable(booker));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.ofNullable(item));

        List<BookingDto> actualList = bookingService.getBookingAllItemsByOwnerId(
                owner.getId(), "ALL", 0L, 22L);

        assertEquals(BookingMapper.mapToBookingDto(bookings, item, owner), actualList);
    }

    @Test
    void updateBooking() {
        bookingService.updateBooking(user.getId(), item.getId(), "true");

        verify(bookingRepositoryImpl).update(user.getId(), item.getId(), "true");
    }
}