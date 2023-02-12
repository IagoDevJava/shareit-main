package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    @Test
    void toUserDto() {
        User user = User.builder().id(0L).name("name").email("name@mail.ru").build();
        UserDto userDto = UserDto.builder().id(0L).name("name").email("name@mail.ru").build();

        UserDto expectedUserDto = UserMapper.toUserDto(user);

        assertEquals(expectedUserDto, userDto);
    }

    @Test
    void mapToUserDto() {
        List<User> users = new ArrayList<>();
        User user = User.builder().id(0L).name("name").email("name@mail.ru").build();
        users.add(user);
        List<UserDto> usersDto = new ArrayList<>();
        UserDto userDto = UserDto.builder().id(0L).name("name").email("name@mail.ru").build();
        usersDto.add(userDto);

        List<UserDto> expectedListUserDto = UserMapper.mapToUserDto(users);

        assertEquals(expectedListUserDto, usersDto);
    }
}