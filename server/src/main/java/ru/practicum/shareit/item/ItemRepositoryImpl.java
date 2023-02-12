package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.OwnerNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ItemRepositoryImpl {
    private final ItemRepository repository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemRepositoryImpl(@Lazy ItemRepository repository,
                              CommentRepository commentRepository,
                              UserRepository userRepository) {
        this.repository = repository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    /**
     * Редактирование вещи в БД
     */
    public ItemDto update(Long userId, Long itemId, Item item) {
        Item saveItem = repository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));

        saveItem.setName(item.getName());
        saveItem.setDescription(item.getDescription());
        saveItem.setAvailable(item.getAvailable());
        saveItem.setRequestId(item.getRequestId());

        if (Objects.equals(saveItem.getOwnerId(), userId)) {
            saveItem.setOwnerId(userId);
            repository.save(saveItem);
            log.info("Вещь № {} обновлена", item.getId());

            return updateCommentToItemDto(userId, itemId, saveItem);
        } else {
            log.warn("Пользователь {} не является владельцем вещи", saveItem.getOwnerId());
            throw new OwnerNotFoundException("Пользователь не является владельцем вещи");
        }
    }

    /**
     * Обновление списка отзывов у вещи
     */
    private ItemDto updateCommentToItemDto(Long userId, Long itemId, Item saveItem) {
        List<Comment> commentsByItemId = commentRepository.findCommentsByItemId(itemId);
        List<CommentDto> commentDtos = CommentMapper.mapToCommentDto(commentsByItemId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        for (CommentDto commentDto : commentDtos) {
            commentDto.setAuthorName(user.getName());
        }
        ItemDto itemDto = ItemMapper.toItemDto(saveItem);
        itemDto.setComments(commentDtos);
        return itemDto;
    }
}