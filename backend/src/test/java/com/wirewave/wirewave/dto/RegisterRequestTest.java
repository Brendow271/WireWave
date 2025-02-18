package com.wirewave.wirewave.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegisterRequestTest {

    @Test
    void testRegisterRequestGettersAndSetters() {
        // Создаем объект RegisterRequest
        RegisterRequest registerRequest = new RegisterRequest();

        // Устанавливаем значения
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("john.doe@example.com");
        registerRequest.setPassword("strongPassword");

        // Проверяем корректность работы геттеров
        assertEquals("John", registerRequest.getFirstName());
        assertEquals("Doe", registerRequest.getLastName());
        assertEquals("john.doe@example.com", registerRequest.getEmail());
        assertEquals("strongPassword", registerRequest.getPassword());
    }
}
