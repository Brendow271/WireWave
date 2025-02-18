package com.wirewave.wirewave.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginRequestTest {

    @Test
    void testLoginRequestGettersAndSetters() {
        // Создаем объект LoginRequest
        LoginRequest loginRequest = new LoginRequest();

        // Устанавливаем значения
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("securePassword");

        // Проверяем корректность работы геттеров
        assertEquals("test@example.com", loginRequest.getEmail());
        assertEquals("securePassword", loginRequest.getPassword());
    }
}