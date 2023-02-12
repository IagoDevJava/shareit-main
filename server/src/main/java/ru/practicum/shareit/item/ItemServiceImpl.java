package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.exception.AddCommentWithoutBookingException;
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
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final ItemRepositoryImpl itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository repository,
                           ItemRepositoryImpl itemRepository,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository,
                           UserRepository userRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    /**
     * Добавление вещи в БД
     */
    @Override
    public ItemDto addItem(Long userId, Item item) {
        if (userRepository.findById(userId).isPresent()) {
            item.setOwnerId(userId);
            Item saveItem = repository.save(item);
            ItemDto itemDto = ItemMapper.toItemDto(saveItem);
            log.info("Вещь № {} добавлена, owner {}", item.getId(), item.getOwnerId());
            return itemDto;
        } else {
            throw new UserNotFoundException("Такого пользователя нет в базе");
        }
    }

    /**
     * Редактирование вещи в БД
     */
    @Override
    public ItemDto editItem(Long userId, Long itemId, Item item) {
        return itemRepository.update(userId, itemId, item);
    }

    /**
     * Получение информации о вещи в БД
     */
    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Такой вещи не существует"));
        ItemDto itemDto = ItemMapper.toItemDto(item);

        updateCommentsOfItem(itemId, itemDto);

        if (userId.equals(item.getOwnerId())) {
            List<Booking> bookingByItemId = bookingRepository.findBookingByItemId(itemId);
            Collections.sort(bookingByItemId);
            if (!bookingByItemId.isEmpty()) {
                itemDto.setNextBooking(BookingMapper.toBookingDtoItem(bookingByItemId.get(0)));
                itemDto.setLastBooking(BookingMapper.toBookingDtoItem(bookingByItemId.get(bookingByItemId.size() - 1)));
            }
            log.info("Получили вещь с датами № {}", itemId);
            return itemDto;
        } else {
            List<Booking> bookingByItemId = bookingRepository.findBookingByItemId(itemId);
            Collections.sort(bookingByItemId);
            log.info("Получили вещь без дат № {}", itemId);
            return itemDto;
        }
    }

    /**
     * Получение владельцем списка его вещей в БД
     */
    @Override
    public List<ItemDto> getItems(Long userId) {
        List<Item> itemByOwnerId = repository.findItemByOwnerId(userId);
        List<ItemDto> result = new ArrayList<>();
        if (!itemByOwnerId.isEmpty()) {
            for (Item item : itemByOwnerId) {
                List<Booking> bookingByItemId = bookingRepository.findBookingByItemId(item.getId());
                Collections.sort(bookingByItemId);
                ItemDto itemDto = ItemMapper.toItemDto(item);
                if (!bookingByItemId.isEmpty()) {
                    itemDto.setNextBooking(BookingMapper.toBookingDtoItem(bookingByItemId.get(0)));
                    itemDto.setLastBooking(BookingMapper.toBookingDtoItem(bookingByItemId.get(bookingByItemId.size() - 1)));
                }
                result.add(itemDto);
                Collections.sort(result);
            }
        }
        log.info("Пользователь {} получил список своих вещей {}", userId, itemByOwnerId.size());
        return result;
    }

    /**
     * Поиск вещи потенциальным арендатором
     */
    @Override
    public List<ItemDto> getItemsByRequest(String text) {
        List<Item> resultList = new ArrayList<>();
        if (!text.isEmpty()) {
            String lowerText = text.toLowerCase();
            List<Item> itemsByRequest = repository.findItemsByRequest(lowerText);
            resultList.addAll(itemsByRequest);
        }
        return ItemMapper.mapToItemDto(resultList);
    }

    /**
     * Добавление отзыва на вещь
     */
    @Override
    public CommentDto addCommentToItem(Long userId, Long itemId, Comment comment) {
        boolean isBookerId = false;
        ItemDto itemDto = getItemById(userId, itemId);
        List<Booking> bookingByItemId = bookingRepository.findBookingByItemId(itemId);
        List<CommentDto> comments = new ArrayList<>();
        if (comment == null) {
            itemDto.setComments(comments);
        } else {
            for (Booking booking : bookingByItemId) {
                if (userId.equals(booking.getBookerId())
                        && booking.getStatus().equals(Status.APPROVED)
                        && !booking.getStart().isAfter(LocalDateTime.now())) {
                    isBookerId = true;
                    break;
                }
            }
            if (isBookerId) {
                comment.setItemId(itemId);
                comment.setCreated(LocalDateTime.now());
                comment.setAuthorId(userId);
                commentRepository.save(comment);
                comments.add(CommentMapper.toCommentDto(comment));
                itemDto.setComments(comments);
            } else {
                throw new AddCommentWithoutBookingException("Booking not found");
            }
        }
        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        commentDto.setAuthorName(
                userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"))
                        .getName());
        return commentDto;
    }

    /**
     * Обновление списка отзывов у вещи
     */
    private void updateCommentsOfItem(Long itemId, ItemDto itemDto) {
        List<Comment> commentsByItemId = commentRepository.findCommentsByItemId(itemId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : commentsByItemId) {
            Long authorId = comment.getAuthorId();
            User user = userRepository.findById(authorId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            CommentDto commentDto = CommentMapper.toCommentDto(comment);
            commentDto.setAuthorName(user.getName());
            commentDtos.add(commentDto);
        }
        itemDto.setComments(commentDtos);
    }
}