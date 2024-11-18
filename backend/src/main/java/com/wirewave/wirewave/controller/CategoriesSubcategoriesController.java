package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.entity.CategoriesSubcategories;
import com.wirewave.wirewave.service.CategoriesSubcategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories-subcategories")
public class CategoriesSubcategoriesController {

    @Autowired
    private CategoriesSubcategoriesService categoriesSubcategoriesService;

    // Создание связи между категорией и подкатегорией
    @PostMapping
    public ResponseEntity<CategoriesSubcategories> createCategoriesSubcategories(@Valid @RequestBody CategoriesSubcategories categoriesSubcategories) {
        CategoriesSubcategories savedCategoriesSubcategories = categoriesSubcategoriesService.saveCategoriesSubcategories(categoriesSubcategories);
        return ResponseEntity.ok(savedCategoriesSubcategories);
    }

    // Получение всех связей между категориями и подкатегориями
    @GetMapping
    public ResponseEntity<List<CategoriesSubcategories>> getAllCategoriesSubcategories() {
        List<CategoriesSubcategories> categoriesSubcategories = categoriesSubcategoriesService.getAllCategoriesSubcategories();
        return ResponseEntity.ok(categoriesSubcategories);
    }

    // Получение связи между категорией и подкатегорией по ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriesSubcategories> getCategoriesSubcategoriesById(@PathVariable Integer id) {
        CategoriesSubcategories categoriesSubcategories = categoriesSubcategoriesService.getCategoriesSubcategoriesById(id);
        return categoriesSubcategories != null ? ResponseEntity.ok(categoriesSubcategories) : ResponseEntity.notFound().build();
    }

    // Обновление связи между категорией и подкатегорией
    @PutMapping("/{id}")
    public ResponseEntity<CategoriesSubcategories> updateCategoriesSubcategories(@PathVariable Integer id, @Valid @RequestBody CategoriesSubcategories categoriesSubcategoriesDetails) {
        CategoriesSubcategories categoriesSubcategories = categoriesSubcategoriesService.getCategoriesSubcategoriesById(id);
        if (categoriesSubcategories == null) {
            return ResponseEntity.notFound().build();
        }

        categoriesSubcategories.setCategory(categoriesSubcategoriesDetails.getCategory());
        categoriesSubcategories.setSubcategory(categoriesSubcategoriesDetails.getSubcategory());

        CategoriesSubcategories updatedCategoriesSubcategories = categoriesSubcategoriesService.saveCategoriesSubcategories(categoriesSubcategories);
        return ResponseEntity.ok(updatedCategoriesSubcategories);
    }

    // Удаление связи между категорией и подкатегорией по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoriesSubcategories(@PathVariable Integer id) {
        if (categoriesSubcategoriesService.getCategoriesSubcategoriesById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        categoriesSubcategoriesService.deleteCategoriesSubcategories(id);
        return ResponseEntity.noContent().build();
    }
}
