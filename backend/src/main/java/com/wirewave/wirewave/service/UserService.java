package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.User;
import com.wirewave.wirewave.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Реализация метода loadUserByUsername для загрузки пользователя по email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с email " + email + " не найден"));
    }

    // Получение всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Получение пользователя по ID
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    // Поиск пользователя по email
    public Optional<User> getUserByEmail(String email) {
        try {
            return Optional.of((User) loadUserByUsername(email));
        } catch (UsernameNotFoundException e) {
            return Optional.empty();
        }
    }

    // Сохранение пользователя
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Удаление пользователя по ID
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Хэширует пароль с использованием PasswordEncoder (алгоритм BCrypt)
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // Проверяет соответствие введённого пароля и хэшированного пароля
    public boolean validatePassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

}