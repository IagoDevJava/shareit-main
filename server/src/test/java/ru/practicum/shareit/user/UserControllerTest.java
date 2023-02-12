package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserServiceImpl userServiceImpl;
    @InjectMocks
    private UserController userController;
    User expectedUser;
    UserDto expectedUserDto;

    @BeforeEach
    void setUp() {
        expectedUser = User.builder().id(1L).name("1name").email("1@mail.ru").build();
        expectedUserDto = UserMapper.toUserDto(expectedUser);
    }

    @Test
    void saveUser() {
        Mockito.when(userServiceImpl.saveUser(expectedUser)).thenReturn(expectedUserDto);

        UserDto actualUserDto = userController.saveUser(expectedUser);

        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void updateUser() {
        User expectedUpdateUser = User.builder().id(1L).name("updateName").email("1@mail.ru").build();
        UserDto expectedUserUpdateDto = UserMapper.toUserDto(expectedUpdateUser);
        Mockito.when(userServiceImpl.updateUser(expectedUser.getId(), expectedUpdateUser)).thenReturn(expectedUserUpdateDto);

        UserDto actualUpdateUserDto = userController.updateUser(expectedUser.getId(), expectedUpdateUser);

        assertEquals(expectedUserUpdateDto, actualUpdateUserDto);
    }

    @Test
    void getUsers() {
        List<UserDto> expectedUsers = List.of(new UserDto());
        Mockito.when(userServiceImpl.getUsers()).thenReturn(expectedUsers);

        List<UserDto> users = userController.getUsers();

        assertEquals(expectedUsers, users);
    }

    @Test
    void findUserById() {
        Mockito.when(userServiceImpl.findUserById(expectedUser.getId())).thenReturn(expectedUserDto);

        UserDto actualUserDto = userController.findUserById(expectedUser.getId());

        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void deleteUsers() {
        userController.saveUser(expectedUser);

        userController.deleteUsers();

        ArrayList<Object> expected = new ArrayList<>();
        List<UserDto> actual = userController.getUsers();
        assertEquals(expected, actual);
    }

    @Test
    void deleteUserById() {
        userController.saveUser(expectedUser);

        userController.deleteUserById(1L);

        ArrayList<Object> expected = new ArrayList<>();
        List<UserDto> actual = userController.getUsers();
        assertEquals(expected, actual);
    }
}