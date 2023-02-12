package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    UserRepositoryImpl userRepositoryImpl;
    User user;
    Long userId;

    @BeforeEach
    void setUp() {
        userId = 0L;
        user = User.builder()
                .id(0L)
                .name("name")
                .email("name@mail.ru")
                .build();
    }

    @Test
    void saveUser_whenUserValid() {
        when(userRepository.save(user)).thenReturn(user);

        UserDto expectedUserDto = userServiceImpl.saveUser(user);

        assertEquals(expectedUserDto, UserMapper.toUserDto(user));
        verify(userRepository).save(user);
    }

    @Test
    void getUsers_whenListContainsUser() {
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<UserDto> users = userServiceImpl.getUsers();

        assertEquals(UserMapper.mapToUserDto(expectedUsers), users);
    }

    @Test
    void getUsers_whenListIsEmpty() {
        ArrayList<User> expected = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(expected);

        List<UserDto> users = userServiceImpl.getUsers();

        assertEquals(UserMapper.mapToUserDto(expected), users);
    }

    @Test
    void findUserById_whenUserFound_thenReturnUserDto() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto actualUserDto = userServiceImpl.findUserById(userId);

        assertEquals(UserMapper.toUserDto(user), actualUserDto);
    }

    @Test
    void findUserById_whenUserNotFound_thenReturnUserNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.findUserById(userId));
    }

    @Test
    void deleteUsers() {
        userServiceImpl.deleteUsers();

        verify(userRepository).deleteAll();
    }

    @Test
    void deleteUserById() {
        userServiceImpl.deleteUserById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void updateUser() {
        userServiceImpl.updateUser(userId, user);

        verify(userRepositoryImpl).update(userId, user);
    }
}