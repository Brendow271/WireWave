package com.wirewave.wirewave.repository;

import com.wirewave.wirewave.entity.Category;
import com.wirewave.wirewave.entity.ProductCategories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoriesRepository extends JpaRepository<ProductCategories, Integer> {

    // Поиск всех продуктов, принадлежащих указанной категории
    List<ProductCategories> findByCategory(Category category);

    // Поиск всех категорий, в которых есть указанный продукт
    List<ProductCategories> findByProductId(Integer productId);
}