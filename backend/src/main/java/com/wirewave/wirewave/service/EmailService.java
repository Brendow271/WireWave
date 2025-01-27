package com.wirewave.wirewave.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // Формирование и вывод email-сообщения
    public void sendOrderEmail(String recipientEmail, String subject, String body) {
        System.out.println("=== Отправка письма на почту ===");
        System.out.println("Получатель: " + recipientEmail);
        System.out.println("Тема: " + subject);
        System.out.println("Содержание:");
        System.out.println(body);
        System.out.println("===============================");
    }
}
