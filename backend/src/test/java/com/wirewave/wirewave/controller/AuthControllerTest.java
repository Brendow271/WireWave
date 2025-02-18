package com.wirewave.wirewave.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirewave.wirewave.config.JwtUtil;
import com.wirewave.wirewave.dto.LoginRequest;
import com.wirewave.wirewave.dto.RegisterRequest;
import com.wirewave.wirewave.entity.User;
import com.wirewave.wirewave.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@Import(JwtUtil.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("johndoe@example.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("johndoe@example.com");
        loginRequest.setPassword("password123");

        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setRole(User.Role.USER);
    }

    // Тест на успешную регистрацию
    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        when(userService.getUserByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(userService.saveUser(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("hashed_password");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Регистрация прошла успешно"));
    }

    // Тест на регистрацию при уже существующем email
    @Test
    void shouldReturnBadRequestWhenEmailAlreadyExists() throws Exception {
        when(userService.getUserByEmail(registerRequest.getEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email уже используется"));
    }

    // Тест на успешный вход
    @Test
    void shouldLoginUserSuccessfully() throws Exception {
        when(userService.getUserByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(userService.validatePassword(loginRequest.getPassword(), user.getHashedPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user.getEmail())).thenReturn("mocked_jwt_token");

        mockMvc.perform(post("/api/auth/user-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("mocked_jwt_token"));
    }

    // Тест на вход с неверным паролем
    @Test
    void shouldReturnBadRequestWhenPasswordIsInvalid() throws Exception {
        when(userService.getUserByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(userService.validatePassword(loginRequest.getPassword(), user.getHashedPassword())).thenReturn(false);

        mockMvc.perform(post("/api/auth/user-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Неверный email или пароль"));
    }

    // Тест на вход с несуществующим пользователем
    @Test
    void shouldReturnBadRequestWhenUserNotFound() throws Exception {
        when(userService.getUserByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/user-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Неверный email или пароль"));
    }

    // Тест на вход с некорректными данными (пустые поля)
    @Test
    void shouldReturnBadRequestWhenLoginWithInvalidData() throws Exception {
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setEmail("");
        invalidRequest.setPassword("");

        mockMvc.perform(post("/api/auth/user-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
