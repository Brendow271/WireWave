package com.wirewave.wirewave.entity; //app_user

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    @Email
    @NotEmpty
    @Size(max = 255)
    private String email;

    @Column(nullable = false)
    @NotEmpty
    @Size(max = 255)
    private String hashPassword;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER,
        ADMIN
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashPassword;
    }

    public void setPasswordHash(String passwordHash) {
        this.hashPassword = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.hashPassword = hashPassword(password);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
