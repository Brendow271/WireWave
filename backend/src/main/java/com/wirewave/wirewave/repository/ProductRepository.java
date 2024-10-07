package com.wirewave.wirewave.repository;

import com.wirewave.wirewave.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}