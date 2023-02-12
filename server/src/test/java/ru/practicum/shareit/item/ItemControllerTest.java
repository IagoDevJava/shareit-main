package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemServiceImpl itemService;
    @InjectMocks
    private ItemController itemController;
    Item item;
    ItemDto itemDto;
    Long userId;
    LocalDateTime time;

    @BeforeEach
    void setUp() {
        userId = 1L;
        time = LocalDateTime.now();
        item = Item.builder()
                .id(0L)
                .name("name")
                .description("description")
                .requestId(1L)
                .ownerId(2L)
                .available(true)
                .build();
        itemDto = ItemMapper.toItemDto(item);
    }

    @Test
    void addItem() {
        when(itemService.addItem(userId, item)).thenReturn(itemDto);

        ItemDto actualItemDto = itemController.addItem(userId, item);

        assertEquals(itemDto, actualItemDto);
    }

    @Test
    void editItem() {
        Item newItem = Item.builder()
                .id(0L)
                .name("updateName")
                .description("description")
                .requestId(1L)
                .ownerId(2L)
                .available(true)
                .build();
        when(itemService.editItem(userId, item.getId(), newItem)).thenReturn(ItemMapper.toItemDto(newItem));

        ItemDto actualItemDto = itemController.editItem(userId, item.getId(), newItem);

        assertEquals(itemDto, actualItemDto);
    }

    @Test
    void getItemById() {
        when(itemService.getItemById(userId, item.getId())).thenReturn(itemDto);

        ItemDto actualItem = itemController.getItemById(userId, item.getId());

        assertEquals(itemDto, actualItem);
    }

    @Test
    void getItems() {
        ArrayList<ItemDto> itemsDto = new ArrayList<>();
        when(itemService.getItems(userId)).thenReturn(itemsDto);

        List<ItemDto> items = itemController.getItems(userId);

        assertEquals(itemsDto, items);
    }

    @Test
    void getItemsByRequest() {
        ArrayList<ItemDto> itemsByRequest = new ArrayList<>();
        when(itemService.getItemsByRequest("text")).thenReturn(itemsByRequest);

        List<ItemDto> itemsDtoByText = itemController.getItemsByRequest("text");

        assertEquals(itemsByRequest, itemsDtoByText);
    }

    @Test
    void addCommentToItem() {
        Comment comment = Comment.builder()
                .id(1L)
                .text("text")
                .itemId(item.getId())
                .authorId(userId)
                .created(time)
                .build();
        when(itemService.addCommentToItem(userId, item.getId(), comment))
                .thenReturn(CommentMapper.toCommentDto(comment));

        CommentDto actualCommentDto = itemController.addCommentToItem(userId, item.getId(), comment);

        assertEquals(CommentMapper.toCommentDto(comment), actualCommentDto);
    }
}