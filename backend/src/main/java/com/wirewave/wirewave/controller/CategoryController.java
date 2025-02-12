package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.entity.Category;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.repository.CategoryRepository;
import com.wirewave.wirewave.repository.ProductCategoriesRepository;
import com.wirewave.wirewave.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductCategoriesRepository productCategoriesRepository;

    // Получение всех подкатегорий для заданной категории рекурсивно.
    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<List<Category>> getAllSubcategories(@PathVariable Integer categoryId) {
        Set<Category> allSubcategories = new HashSet<>();
        findSubcategoriesRecursive(categoryId, allSubcategories);
        return ResponseEntity.ok(new ArrayList<>(allSubcategories));
    }

    // Рекурсивный метод для поиска всех подкатегорий.
    private void findSubcategoriesRecursive(Integer categoryId, Set<Category> allSubcategories) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            allSubcategories.add(category.get());

            List<Category> subcategories = categoryRepository.findSubcategoriesByCategory(category.get());
            for (Category subcategory : subcategories) {
                if (!allSubcategories.contains(subcategory)) {
                    findSubcategoriesRecursive(subcategory.getId(), allSubcategories);
                }
            }
        }
    }

    // Создание новой категории
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        Category savedCategory = categoryService.saveCategory(category);
        return ResponseEntity.ok(savedCategory);
    }

    // Получение всех категорий
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Получение категории по ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }

    // Обновление данных категории
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @Valid @RequestBody Category categoryDetails) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        category.setCategoryName(categoryDetails.getCategoryName());

        Category updatedCategory = categoryService.saveCategory(category);
        return ResponseEntity.ok(updatedCategory);
    }

    // Удаление категории по ID
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        if (categoryService.getCategoryById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
