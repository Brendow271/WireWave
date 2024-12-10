package com.wirewave.wirewave.repository;

import com.wirewave.wirewave.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT cs.subcategory FROM CategoriesSubcategories cs WHERE cs.category = :category")
    List<Category> findSubcategoriesByCategory(@Param("category") Category category);

}