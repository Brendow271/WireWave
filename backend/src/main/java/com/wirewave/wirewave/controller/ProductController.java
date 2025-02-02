package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.entity.Category;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.Warehouse;
import com.wirewave.wirewave.service.FilterService;
import com.wirewave.wirewave.service.ProductService;
import com.wirewave.wirewave.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private FilterService filterService;

    @GetMapping("/{id}/stock")
    public ResponseEntity<Integer> getProductStock(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        int totalStock = warehouseService.getTotalStockForProduct(product);
        return ResponseEntity.ok(totalStock);
    }

    // Создание нового продукта
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, @RequestParam Integer initialQuantity) {
        Product savedProduct = productService.saveProduct(product);
        Warehouse warehouse = new Warehouse();
        warehouse.setProduct(savedProduct);
        warehouse.setQuantity(initialQuantity);
        warehouseService.saveWarehouse(warehouse);

        return ResponseEntity.ok(savedProduct);
    }

    // Получение всех продуктов
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Получение продукта по ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // Получение средней оценки продукта
    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> getProductRatingById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product.getAverageRating());
    }

    // Обновление продукта
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @Valid @RequestBody Product productDetails) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        product.setProductName(productDetails.getProductName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setDiscountPrice(productDetails.getDiscountPrice());
        product.setImageUrl(productDetails.getImageUrl());

        Product updatedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    // Удаление продукта по ID
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        if (productService.getProductById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Получение всех категорий продукта
    @GetMapping("/{id}/categories")
    public ResponseEntity<List<Category>> getProductCategories(@PathVariable Integer id) {
        List<Category> categories = productService.getCategoriesByProduct(id);
        if (categories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }

    // Обновлённый фильтр товаров
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false, defaultValue = "") List<Integer> attributeIds,
            @RequestParam(required = false, defaultValue = "") List<String> attributeValues) {

        // Если список пустой, заменяем на `null`
        List<Integer> validAttributeIds = (attributeIds.isEmpty() ? null : attributeIds);
        List<String> validAttributeValues = (attributeValues.isEmpty() ? null : attributeValues);

        List<Product> products = filterService.filterProducts(categoryId, minPrice, maxPrice, minRating, validAttributeIds, validAttributeValues);
        return ResponseEntity.ok(products);
    }
}
