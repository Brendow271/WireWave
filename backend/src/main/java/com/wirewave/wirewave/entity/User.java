
package com.wirewave.wirewave.entity;

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
    private Integer id;

    @Column(name = "token")
    private Integer token;

    @Column(name = "first_name", nullable = false)
    @NotEmpty
    @Size(max = 255)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotEmpty
    @Size(max = 255)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotEmpty
    @Size(max = 255)
    private String email;

    @Column(name = "hashed_password", nullable = false)
    @NotEmpty
    @Size(max = 255)
    private String hashedPassword;

    public void setToken(Integer token) {
        this.token = token;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public Integer getToken() {
        return token;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setPassword(String password) {
        this.hashedPassword = hashPassword(password);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}