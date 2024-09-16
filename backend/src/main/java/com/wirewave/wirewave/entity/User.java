package com.wirewave.wirewave.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "token")
    private Integer token;

    @Column(name = "first_name", nullable = false)
    @Size(max = 255)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(max = 255)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @Size(max = 255)
    private String email;

    @Column(name = "password", nullable = false)
    @Size(max = 255)
    private String password;

}