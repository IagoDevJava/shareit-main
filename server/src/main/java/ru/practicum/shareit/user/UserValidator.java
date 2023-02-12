package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

@Slf4j
public class UserValidator {
    /**
     * Валидация id пользователей
     */
    public static void isValidIdUsers(long id) {
        if (id < 0) {
            log.warn("Id пользователя отрицательный");
            throw new UserNotFoundException(String.format("Id пользователя %d отрицательный", id));
        }
    }

    /**
     * Валидация email пользователей при создании
     */
    public static void isValidEmailUser(User user) throws ValidationException {
        if (user.getEmail().isBlank()) {
            log.warn("Ошибка, email не должен быть пустым: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "email не должен быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Ошибка, email неверен: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "email должен содержать - \"@\"");
        }
    }

    /**
     * Проверка на null имени и email пользователей при создании
     */
    public static void isValidUserToNull(User user) {
        if (user.getEmail() == null) {
            log.warn("Ошибка в email: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "email должен быть");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пусто: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "name не должен быть пустым");
        }
    }
}