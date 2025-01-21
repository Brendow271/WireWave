package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.entity.ProductCategories;
import com.wirewave.wirewave.service.ProductCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoriesController {

    @Autowired
    private ProductCategoriesService productCategoriesService;

    // Создание новой связи между продуктом и категорией
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductCategories> createProductCategory(@Valid @RequestBody ProductCategories productCategories) {
        ProductCategories savedProductCategory = productCategoriesService.saveProductCategory(productCategories);
        return ResponseEntity.ok(savedProductCategory);
    }

    // Получение всех связей между продуктами и категориями
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<ProductCategories>> getAllProductCategories() {
        List<ProductCategories> productCategories = productCategoriesService.getAllProductCategories();
        return ResponseEntity.ok(productCategories);
    }

    // Получение связи по ID
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategories> getProductCategoryById(@PathVariable Integer id) {
        ProductCategories productCategory = productCategoriesService.getProductCategoryById(id);
        return productCategory != null ? ResponseEntity.ok(productCategory) : ResponseEntity.notFound().build();
    }

    // Обновление связи между продуктом и категорией
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductCategories> updateProductCategory(@PathVariable Integer id, @Valid @RequestBody ProductCategories productCategoryDetails) {
        ProductCategories productCategory = productCategoriesService.getProductCategoryById(id);
        if (productCategory == null) {
            return ResponseEntity.notFound().build();
        }

        productCategory.setCategory(productCategoryDetails.getCategory());
        productCategory.setProduct(productCategoryDetails.getProduct());

        ProductCategories updatedProductCategory = productCategoriesService.saveProductCategory(productCategory);
        return ResponseEntity.ok(updatedProductCategory);
    }

    // Удаление связи по ID
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Integer id) {
        if (productCategoriesService.getProductCategoryById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        productCategoriesService.deleteProductCategory(id);
        return ResponseEntity.noContent().build();
    }
}
