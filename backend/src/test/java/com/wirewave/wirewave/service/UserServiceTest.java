package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.User;
import com.wirewave.wirewave.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setRole(User.Role.USER);
    }

    // Тест на получение всех пользователей
    @Test
    void shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0)).isEqualTo(user);
    }

    // Тест на получение пользователя по email
    @Test
    void shouldReturnUserByEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserByEmail(user.getEmail());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    // Тест на ошибку, если пользователя нет
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername(user.getEmail()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Пользователь с email " + user.getEmail() + " не найден");
    }

    // Тест на сохранение пользователя
    @Test
    void shouldSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(user.getId());
    }
}
