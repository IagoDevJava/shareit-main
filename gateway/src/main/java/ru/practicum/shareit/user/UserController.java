package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    /**
     * Добавление пользователя в БД
     */
    @PostMapping()
    public ResponseEntity<Object> saveUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("Add user {}", userRequestDto.getName());
        return userClient.saveUser(userRequestDto);
    }

    /**
     * Обновление пользователя
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PositiveOrZero @PathVariable Long userId,
                                             @RequestBody UserRequestDto userRequestDto) {
        log.info("Update user with user id {}", userId);
        return userClient.updateUser(userId, userRequestDto);
    }

    /**
     * Получение списка пользователей
     */
    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Get all users");
        return userClient.getUsers();
    }

    /**
     * Получение пользователя по id
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PositiveOrZero @PathVariable Long userId) {
        log.info("Get user with id {}", userId);
        return userClient.getUser(userId);
    }

    /**
     * Удаление пользователя по id
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PositiveOrZero @PathVariable Long userId) {
        log.info("Delete user with id {}", userId);
        return userClient.deleteUser(userId);
    }
}
