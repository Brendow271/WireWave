package com.wirewave.wirewave.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirewave.wirewave.config.JwtAuthenticationFilter;
import com.wirewave.wirewave.config.SecurityConfiguration;
import com.wirewave.wirewave.entity.User;
import com.wirewave.wirewave.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    // Тест на ошибку при дублирующемся email
    @Test
    void shouldReturnBadRequestIfUserExists() throws Exception {
        when(userService.getUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    // Тест на получение всех пользователей
    @Test
    void shouldGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].email").value(user.getEmail()));
    }

    // Тест на получение пользователя по ID
    @Test
    void shouldGetUserById() throws Exception {
        when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    // Тест на получение пользователя по ID, если его нет
    @Test
    void shouldReturnNotFoundIfUserNotExists() throws Exception {
        when(userService.getUserById(1)).thenReturn(null);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    // Тест на удаление пользователя
    @Test
    void shouldDeleteUser() throws Exception {
        when(userService.getUserById(1)).thenReturn(user);
        Mockito.doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    // Тест на удаление несуществующего пользователя
    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentUser() throws Exception {
        when(userService.getUserById(1)).thenReturn(null);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());
    }
}
