package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {
    @InjectMocks
    UserRepositoryImpl userRepository;
    @Mock
    UserRepository repository;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    User user;
    User userUpdateName;
    User userUpdateEmail;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(0L)
                .name("name")
                .email("name@mail.ru")
                .build();
        userUpdateName = User.builder()
                .name("updateName")
                .build();
        userUpdateEmail = User.builder()
                .email("updateEmail@mail.ru")
                .build();
    }

    @Test
    void updateNameUser() {
        when(repository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        userRepository.update(user.getId(), userUpdateName);

        verify(repository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("updateName", savedUser.getName());
    }

    @Test
    void updateEmailUser() {
        when(repository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        userRepository.update(user.getId(), userUpdateEmail);

        verify(repository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("updateEmail@mail.ru", savedUser.getEmail());

    }
}