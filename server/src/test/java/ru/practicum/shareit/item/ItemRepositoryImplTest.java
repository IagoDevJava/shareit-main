package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRepositoryImplTest {
    @InjectMocks
    ItemRepositoryImpl itemRepository;
    @Mock
    ItemRepository repository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    UserRepository userRepository;
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    Item item;
    Item itemUpdateName;
    Item itemUpdateDescription;
    Item itemUpdateAvailable;
    User user;
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
        itemUpdateName = Item.builder()
                .id(1L)
                .name("updateName")
                .description("description")
                .requestId(1L)
                .ownerId(2L)
                .available(true)
                .build();
        itemUpdateDescription = Item.builder()
                .id(1L)
                .name("updateName")
                .description("updateDescription")
                .requestId(1L)
                .ownerId(2L)
                .available(true)
                .build();
        itemUpdateAvailable = Item.builder()
                .id(1L)
                .name("updateName")
                .description("description")
                .requestId(1L)
                .ownerId(2L)
                .available(false)
                .build();
        user = User.builder()
                .id(2L)
                .name("name")
                .email("name@email.ru")
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
    void updateNameItem() {
        when(repository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        when(commentRepository.findCommentsByItemId(item.getId())).thenReturn(List.of(comment));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        itemRepository.update(user.getId(), item.getId(), itemUpdateName);

        verify(repository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals("updateName", savedItem.getName());
    }

    @Test
    void updateDescriptionItem() {
        when(repository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        when(commentRepository.findCommentsByItemId(item.getId())).thenReturn(List.of(comment));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        itemRepository.update(user.getId(), item.getId(), itemUpdateDescription);

        verify(repository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals("updateDescription", savedItem.getDescription());
    }

    @Test
    void updateAvailableItem() {
        when(repository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        when(commentRepository.findCommentsByItemId(item.getId())).thenReturn(List.of(comment));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        itemRepository.update(user.getId(), item.getId(), itemUpdateAvailable);

        verify(repository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals(false, savedItem.getAvailable());
    }
}