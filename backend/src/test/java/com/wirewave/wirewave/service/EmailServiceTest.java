package com.wirewave.wirewave.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class EmailServiceTest {

    private EmailService emailService;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        emailService = new EmailService();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    // Тест на корректную отправку письма
    @Test
    void shouldSendOrderEmailCorrectly() {
        String recipient = "test@example.com";
        String subject = "Test Subject";
        String body = "This is a test email body.";

        emailService.sendOrderEmail(recipient, subject, body);

        String output = outputStreamCaptor.toString().trim();

        assertThat(output).contains("Отправка письма на почту");
        assertThat(output).contains("Получатель: " + recipient);
        assertThat(output).contains("Тема: " + subject);
        assertThat(output).contains("Содержание:");
        assertThat(output).contains(body);
    }

    // Тест на отправку письма с пустыми полями
    @Test
    void shouldHandleEmptyFields() {
        String recipient = "";
        String subject = "";
        String body = "";

        emailService.sendOrderEmail(recipient, subject, body);

        String output = outputStreamCaptor.toString().trim();

        assertThat(output).contains("Отправка письма на почту");
        assertThat(output).contains("Получатель: ");
        assertThat(output).contains("Тема: ");
        assertThat(output).contains("Содержание:");
    }

    // Тест на отправку письма с длинными строками
    @Test
    void shouldHandleLongStrings() {
        String recipient = "longemail@example.com";
        String subject = "This is a very long subject line that might cause issues if not handled correctly.";
        String body = "This is a very long email body. " +
                "It contains multiple sentences and paragraphs to simulate a real email message. " +
                "We want to make sure that the output is displayed correctly without any truncation or formatting issues.";

        emailService.sendOrderEmail(recipient, subject, body);

        String output = outputStreamCaptor.toString().trim();

        assertThat(output).contains("Отправка письма на почту");
        assertThat(output).contains("Получатель: " + recipient);
        assertThat(output).contains("Тема: " + subject);
        assertThat(output).contains("Содержание:");
        assertThat(output).contains(body);
    }
}
