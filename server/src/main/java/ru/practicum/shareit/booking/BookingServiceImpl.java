package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemAvailabilityException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingRepositoryImpl repositoryImp;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              BookingRepositoryImpl repositoryImp,
                              ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.repositoryImp = repositoryImp;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    /**
     * Добавление нового бронирования
     */
    @Override
    public BookingDto addNewBooking(Long userId, Booking booking) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Item not found"));

        BookingValidator.isValidBooking(booking, user, item);

        if (item.getAvailable()) {
            booking.setStatus(Status.WAITING);
            booking.setBookerId(userId);
            item.setAvailable(false);
            Booking saveBooking = bookingRepository.save(booking);
            log.info("Создали новое бронирование {}", saveBooking.getId());
            return BookingMapper.toBookingDto(saveBooking, item, user);
        } else {
            throw new ItemAvailabilityException("Товар не доступен для бронирования");
        }
    }

    /**
     * Подтверждение или отклонение запроса на бронирование.
     */
    @Override
    public BookingDto updateBooking(Long userId, Long itemId, String approved) {
        return repositoryImp.update(userId, itemId, approved);
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус)
     */
    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        BookingDto bookingDto;
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking id is uncorrected"));
        User booker = userRepository.findById(booking.getBookerId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Item not found"));

        if (Objects.equals(booking.getBookerId(), userId) || item.getOwnerId().equals(userId)) {
            bookingDto = BookingMapper.toBookingDto(booking, item, booker);
            log.info("Получили бронирование №{}", bookingId);
            return bookingDto;
        } else {
            throw new UserNotFoundException("Пользователь не является автором бронирования или владельцем вещи");
        }
    }

    /**
     * Получение списка всех бронирований текущего пользователя.
     */
    @Override
    public List<BookingDto> getBookingsByBookerId(Long userId, String state, Long from, Long size) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Booking> allBookings = new ArrayList<>();
        Pageable pageable = PageRequest.of(
                Math.toIntExact(from),
                Math.toIntExact(size),
                Sort.by("start").descending());

        do {
            Page<Booking> pagesOfBookings = bookingRepository.findAll(pageable);
            List<Booking> bookingList = pagesOfBookings.getContent();
            allBookings.addAll(bookingList);
            if (pagesOfBookings.hasNext()) {
                pageable = pagesOfBookings.nextOrLastPageable();
            } else {
                pageable = null;
            }
        } while (pageable != null);

        List<Booking> bookingsByBookerId = allBookings.stream()
                .filter(booking -> Objects.equals(booking.getBookerId(), userId))
                .collect(Collectors.toList());
        List<Booking> bookingByState = getBookingsByState(state, bookingsByBookerId);
        log.info("Получили список всех бронирований пользователя {}", userId);
        return getListBookingDto(bookingByState);
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя
     */
    @Override
    public List<BookingDto> getBookingAllItemsByOwnerId(Long ownerId, String state, Long from, Long size) {
        userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Booking> result = new ArrayList<>();
        List<Booking> bookingAllItemsByOwnerId = new ArrayList<>();
        List<Item> itemsByOwnerId = itemRepository.findItemByOwnerId(ownerId);

        for (Item item : itemsByOwnerId) {
            List<Booking> bookingByItemId = bookingRepository.findBookingByItemId(item.getId());
            bookingAllItemsByOwnerId.addAll(bookingByItemId);
        }

        Collections.sort(bookingAllItemsByOwnerId);

        List<Booking> bookingsByState = getBookingsByState(state, bookingAllItemsByOwnerId);
        if (bookingsByState.size() - 1 < size) {
            size = (long) bookingsByState.size() - 1;
        }
        for (int i = Math.toIntExact(from); i <= size; i++) {
            result.add(bookingsByState.get(i));
        }

        log.info("Получили список бронирований для всех вещей пользователя {}", ownerId);
        return getListBookingDto(result);
    }

    private List<BookingDto> getListBookingDto(List<Booking> bookings) {
        List<BookingDto> result = new ArrayList<>();

        for (Booking booking : bookings) {
            Item item = itemRepository.findById(booking.getItemId()).orElseThrow();
            User booker = userRepository.findById(booking.getBookerId()).orElseThrow();
            result.add(BookingMapper.toBookingDto(booking, item, booker));
        }

        return result;
    }

    private List<Booking> getBookingsByState(String state, List<Booking> bookings) {
        List<Booking> result = new ArrayList<>();
        switch (state) {
            case ("CURRENT"):
                for (Booking booking : bookings) {
                    if (booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now())) {
                        result.add(booking);
                    }
                }
                log.info("Получили бронирования со статусом {}", state);
                break;
            case ("PAST"):
                for (Booking booking : bookings) {
                    if (booking.getEnd().isBefore(LocalDateTime.now())) {
                        result.add(booking);
                    }
                }
                log.info("Получили бронирования со статусом {}", state);
                break;
            case ("FUTURE"):
                for (Booking booking : bookings) {
                    if (booking.getStart().isAfter(LocalDateTime.now())) {
                        result.add(booking);
                    }
                }
                log.info("Получили бронирования со статусом {}", state);
                break;
            case ("WAITING"):
                for (Booking booking : bookings) {
                    if (booking.getStatus().equals(Status.WAITING)) {
                        result.add(booking);
                    }
                }
                log.info("Получили бронирования со статусом {}", state);
                break;
            case ("REJECTED"):
                for (Booking booking : bookings) {
                    if (booking.getStatus().equals(Status.REJECTED)) {
                        result.add(booking);
                    }
                }
                log.info("Получили бронирования со статусом {}", state);
                break;
            case ("ALL"):
                result.addAll(bookings);
                log.info("Получили бронирования со статусом {}", state);
                break;
        }
        return result;
    }
}