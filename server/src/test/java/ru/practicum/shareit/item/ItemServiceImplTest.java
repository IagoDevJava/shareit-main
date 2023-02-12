package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AddCommentWithoutBookingException;
import ru.practicum.shareit.exception.AddEmptyCommentException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    ItemRepositoryImpl itemRepositoryImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    Item item;
    ItemDto itemDto;
    User user;
    Booking booking;
    Comment comment;
    LocalDateTime time;

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
        itemDto = ItemMapper.toItemDto(item);
        user = User.builder()
                .id(2L)
                .name("name")
                .email("name@email.ru")
                .build();
        booking = Booking.builder()
                .id(1L)
                .status(Status.APPROVED)
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
    }

    @Test
    void addItemIsOk() {
        when(itemRepository.save(item)).thenReturn(item);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ItemDto actualItemDto = itemService.addItem(user.getId(), item);

        assertEquals(itemDto, actualItemDto);
        verify(itemRepository).save(item);
    }

    @Test
    void addItemIsUserIdIsNegative() {
        user.setId(-1L);

        assertThrows(UserNotFoundException.class, () -> itemService.addItem(user.getId(), item));
        verify(itemRepository, never()).save(item);
    }

    @Test
    void addItemIsUserNotFound() {

        assertThrows(UserNotFoundException.class, () -> itemService.addItem(99L, item));
        verify(itemRepository, never()).save(item);
    }

    @Test
    void getItemByIdByOwner() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        when(commentRepository.findCommentsByItemId(item.getId())).thenReturn(new ArrayList<>());
        when(bookingRepository.findBookingByItemId(item.getId())).thenReturn(new ArrayList<>());

        ItemDto actualItemDto = itemService.getItemById(user.getId(), item.getId());

        assertEquals(itemDto, actualItemDto);
    }

    @Test
    void getItemByIdByNotOwner() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        when(commentRepository.findCommentsByItemId(item.getId())).thenReturn(new ArrayList<>());
        when(bookingRepository.findBookingByItemId(item.getId())).thenReturn(new ArrayList<>());

        ItemDto actualItemDto = itemService.getItemById(1L, item.getId());

        assertEquals(itemDto, actualItemDto);
    }

    @Test
    void getItemByWrongId() {
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(user.getId(), 99L));
    }

    @Test
    void getItems() {
        List<Item> expectedItems = new ArrayList<>();
        when(itemRepository.findItemByOwnerId(item.getOwnerId())).thenReturn(expectedItems);
        when(bookingRepository.findBookingByItemId(item.getId())).thenReturn(new ArrayList<>());
        expectedItems.add(item);

        List<ItemDto> items = itemService.getItems(user.getId());

        assertEquals(ItemMapper.mapToItemDto(expectedItems), items);
    }

    @Test
    void getItemsByRequest() {
        String text = "description";
        List<Item> result = List.of(item);
        when(itemRepository.findItemsByRequest(item.getDescription())).thenReturn(result);

        List<ItemDto> itemByText = itemService.getItemsByRequest(text);

        assertEquals(ItemMapper.mapToItemDto(result), itemByText);
    }

    @Test
    void addCommentToItem() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findBookingByItemId(item.getId())).thenReturn(bookings);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        CommentDto actualCommentDto = itemService.addCommentToItem(user.getId(), item.getId(), comment);

        assertEquals(CommentMapper.toCommentDto(comment), actualCommentDto);
        verify(commentRepository).save(comment);
    }

    @Test
    void addCommentToItem_thenReturnAddCommentWithoutBookingException() {
        booking.setBookerId(33L);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findBookingByItemId(item.getId())).thenReturn(bookings);

        assertThrows(AddCommentWithoutBookingException.class,
                () -> itemService.addCommentToItem(user.getId(), item.getId(), comment));
        verify(commentRepository, never()).save(comment);
    }

    @Test
    void editItem() {
        itemService.editItem(user.getId(), item.getId(), item);

        verify(itemRepositoryImpl).update(user.getId(), item.getId(), item);
    }
}