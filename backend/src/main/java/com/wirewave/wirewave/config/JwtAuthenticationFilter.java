package com.wirewave.wirewave.config;

import com.wirewave.wirewave.entity.User;
import com.wirewave.wirewave.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // Основной метод фильтрации запросов с проверкой JWT-токена
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("JWT Authentication Filter: Authorization header is missing or invalid.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);
        String email = null;

        try {
            email = jwtUtil.extractEmail(token);
            System.out.println("JWT Authentication Filter: Extracted email from token: " + email);
        } catch (Exception e) {
            System.out.println("JWT Authentication Filter: Failed to extract email from token. Error: " + e.getMessage());
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = null;
            try {
                user = (User) userService.loadUserByUsername(email);
                System.out.println("JWT Authentication Filter: Loaded user: " + user.getEmail());
            } catch (Exception e) {
                System.out.println("JWT Authentication Filter: Failed to load user. Error: " + e.getMessage());
            }

            if (user != null && jwtUtil.validateToken(token, user.getEmail())) {
                System.out.println("JWT Authentication Filter: Token validated successfully.");
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                System.out.println("JWT Authentication Filter: Token validation failed or user is null.");
            }
        }

        filterChain.doFilter(request, response);
    }

}
