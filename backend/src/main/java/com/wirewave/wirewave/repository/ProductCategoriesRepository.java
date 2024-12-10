package com.wirewave.wirewave.repository;

import com.wirewave.wirewave.entity.Category;
import com.wirewave.wirewave.entity.ProductCategories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoriesRepository extends JpaRepository<ProductCategories, Integer> {

    List<ProductCategories> findByCategory(Category category);

}