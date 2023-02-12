package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Component
public class BookingRepositoryImpl {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingRepositoryImpl(@Lazy BookingRepository bookingRepository,
                                 @Lazy ItemRepository itemRepository,
                                 @Lazy UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    /**
     * Подтверждение или отклонение запроса на бронирование
     */
    public BookingDto update(Long userId, Long bookingId, String approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new BookingChangeApprovedStatusException("Booking approved already");
        } else {
            User booker = userRepository.findById(booking.getBookerId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            Item item = itemRepository.findById(booking.getItemId())
                    .orElseThrow(() -> new ItemNotFoundException("Item not found"));
            if (item.getOwnerId().equals(userId)) {
                if (approved.equals("true")) {
                    booking.setStatus(Status.APPROVED);
                    item.setAvailable(true);
                } else {
                    booking.setStatus(Status.REJECTED);
                    item.setAvailable(true);
                }
                bookingRepository.save(booking);
            } else {
                throw new OwnerNotFoundException("Пользователь не является владельцем вещи");
            }
            log.info("Статус бронирования № {} обновлен", bookingId);
            return BookingMapper.toBookingDto(booking, item, booker);
        }
    }
}