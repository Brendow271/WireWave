package com.wirewave.wirewave.repository;

import com.wirewave.wirewave.entity.ProductCategories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoriesRepository extends JpaRepository<ProductCategories, Integer> {
}