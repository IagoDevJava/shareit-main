package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.CommentRequestDto;
import ru.practicum.shareit.item.model.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    /**
     * Добавление вещи в БД
     */
    @PostMapping()
    public ResponseEntity<Object> saveItem(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") Long userId,
                                           @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Add item {}", itemRequestDto.getName());
        return itemClient.saveItem(userId, itemRequestDto);
    }

    /**
     * Редактирование вещи в БД
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PositiveOrZero @PathVariable Long itemId,
                                             @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Update item № {}", itemRequestDto.getId());
        return itemClient.updateItem(userId, itemId, itemRequestDto);
    }

    /**
     * Получение информации о вещи в БД
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PositiveOrZero @PathVariable Long itemId) {
        log.info("Get item № {}", itemId);
        return itemClient.getItem(userId, itemId);
    }

    /**
     * Получение владельцем списка его вещей в БД
     */
    @GetMapping()
    public ResponseEntity<Object> getItems(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get items for user № {} ", userId);
        return itemClient.getItems(userId);
    }

    /**
     * Поиск вещи потенциальным арендатором
     */
    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByRequest(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam String text) {
        log.info("Get item with text {}", text);
        return itemClient.getItemsByRequest(userId, text);
    }

    /**
     * Добавление отзыва на вещь
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PositiveOrZero @PathVariable Long itemId,
                                              @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return itemClient.saveComment(userId, itemId, commentRequestDto);
    }
}