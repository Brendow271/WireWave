package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.ProductAttribute;
import com.wirewave.wirewave.repository.ProductAttributeRepository;
import com.wirewave.wirewave.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    @Autowired
    private ProductService productService;

    public List<Product> filterProducts(Integer categoryId, Integer minPrice, Integer maxPrice, Integer minRating,
                                        List<Integer> attributeIds, List<String> attributeValues) {
        List<Product> allProducts = productRepository.findAll();


        // Фильтруем по категории (если передана)
        if (categoryId != null) {
            allProducts = allProducts.stream()
                    .filter(product -> productService.getCategoriesByProduct(product.getId()).stream()
                            .anyMatch(category -> category.getId().equals(categoryId)))
                    .collect(Collectors.toList());
        }

        // Фильтруем по цене (если передана)
        if (minPrice != null || maxPrice != null) {
            allProducts = allProducts.stream()
                    .filter(product ->
                            (minPrice == null || (product.getPrice() != null && product.getPrice().compareTo(BigDecimal.valueOf(minPrice)) >= 0)) &&
                                    (maxPrice == null || (product.getPrice() != null && product.getPrice().compareTo(BigDecimal.valueOf(maxPrice)) <= 0)))
                    .collect(Collectors.toList());
        }

        // Фильтруем по рейтингу (если передан, и если у товара есть рейтинг)
        if (minRating != null) {
            allProducts = allProducts.stream()
                    .filter(product -> product.getAverageRating() != null && product.getAverageRating() >= minRating)
                    .collect(Collectors.toList());
        }

        // Фильтруем по атрибутам (тегам) – если переданы
        if ((attributeIds != null && !attributeIds.isEmpty()) || (attributeValues != null && !attributeValues.isEmpty())) {
            List<Integer> productIds = productAttributeRepository.findAll().stream()
                    .filter(attr ->
                            (attributeIds == null || attributeIds.isEmpty() || attributeIds.contains(attr.getAttribute().getId())) &&
                                    (attributeValues == null || attributeValues.isEmpty() || attributeValues.contains(attr.getValue()))
                    )
                    .map(attr -> attr.getProduct().getId())
                    .distinct()
                    .collect(Collectors.toList());

            // Если найдены товары с атрибутами – фильтруем, если нет – возвращаем пустой список
            if (!productIds.isEmpty()) {
                allProducts = allProducts.stream()
                        .filter(product -> productIds.contains(product.getId()))
                        .collect(Collectors.toList());
            } else {
                return List.of();
            }
        }

        return allProducts;
    }



}
