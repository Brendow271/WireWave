package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.dto.LoginRequest;
import com.wirewave.wirewave.dto.RegisterRequest;
import com.wirewave.wirewave.entity.User;
import com.wirewave.wirewave.service.UserService;
import com.wirewave.wirewave.config.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Регистрация
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.getUserByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email уже используется");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword(), passwordEncoder);
        user.setRole(User.Role.USER);
        userService.saveUser(user);

        return ResponseEntity.ok("Регистрация прошла успешно");
    }

    // Вход
    @PostMapping("/user-login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.getUserByEmail(request.getEmail()).orElse(null);
        if (user == null || !userService.validatePassword(request.getPassword(), user.getHashedPassword())) {
            return ResponseEntity.badRequest().body("Неверный email или пароль");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(token);
    }
}
