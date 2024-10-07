package com.wirewave.wirewave.repository;

import com.wirewave.wirewave.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}