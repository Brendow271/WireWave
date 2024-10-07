package com.wirewave.wirewave.repository;

import com.wirewave.wirewave.entity.OrderPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPositionRepository extends JpaRepository<OrderPosition, Integer> {
}