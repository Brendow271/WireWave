package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Category;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.ProductAttribute;
import com.wirewave.wirewave.entity.Attribute;
import com.wirewave.wirewave.repository.ProductAttributeRepository;
import com.wirewave.wirewave.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductAttributeRepository productAttributeRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private FilterService filterService;

    private Product product1;
    private Product product2;
    private ProductAttribute attribute1;
    private ProductAttribute attribute2;
    private Category category;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1);
        product1.setProductName("Product 1");
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setAverageRating(4.5);

        product2 = new Product();
        product2.setId(2);
        product2.setProductName("Product 2");
        product2.setPrice(BigDecimal.valueOf(200));
        product2.setAverageRating(3.0);

        Attribute attr = new Attribute();
        attr.setId(1);
        attr.setName("Color");
        attr.setType("string");

        attribute1 = new ProductAttribute();
        attribute1.setId(1);
        attribute1.setProduct(product1);
        attribute1.setAttribute(attr);
        attribute1.setValue("Red");

        attribute2 = new ProductAttribute();
        attribute2.setId(2);
        attribute2.setProduct(product2);
        attribute2.setAttribute(attr);
        attribute2.setValue("Blue");

        category = new Category();
        category.setId(1);
        category.setCategoryName("Electronics");
    }

    // Тест на фильтрацию без параметров (возвращает все продукты)
    @Test
    void shouldReturnAllProductsWhenNoFiltersApplied() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> result = filterService.filterProducts(null, null, null, null, null, null);

        assertThat(result).containsExactlyInAnyOrder(product1, product2);
    }

    // Тест на фильтрацию по категории
    @Test
    void shouldFilterByCategory() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        when(productService.getCategoriesByProduct(product1.getId())).thenReturn(List.of(category));
        when(productService.getCategoriesByProduct(product2.getId())).thenReturn(List.of());

        List<Product> result = filterService.filterProducts(1, null, null, null, null, null);

        assertThat(result).containsOnly(product1);
    }

    // Тест на фильтрацию по цене
    @Test
    void shouldFilterByPrice() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> result = filterService.filterProducts(null, 150, 250, null, null, null);

        assertThat(result).containsOnly(product2);
    }

    // Тест на фильтрацию по минимальному рейтингу
    @Test
    void shouldFilterByMinRating() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> result = filterService.filterProducts(null, null, null, 4, null, null);

        assertThat(result).containsOnly(product1);
    }

    // Тест на фильтрацию по атрибутам
    @Test
    void shouldFilterByAttribute() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        when(productAttributeRepository.findAll()).thenReturn(List.of(attribute1, attribute2));

        List<Product> result = filterService.filterProducts(null, null, null, null, List.of(1), List.of("Red"));

        assertThat(result).containsOnly(product1);
    }

    // Тест на фильтрацию по атрибутам, когда ничего не найдено
    @Test
    void shouldReturnEmptyListWhenNoMatchingAttributes() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        when(productAttributeRepository.findAll()).thenReturn(List.of(attribute1, attribute2));

        List<Product> result = filterService.filterProducts(null, null, null, null, List.of(99), List.of("Green"));

        assertThat(result).isEmpty();
    }

    // Тест на фильтрацию по нескольким критериям одновременно
    @Test
    void shouldFilterByMultipleCriteria() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        when(productService.getCategoriesByProduct(product1.getId())).thenReturn(List.of(category));
        when(productAttributeRepository.findAll()).thenReturn(List.of(attribute1, attribute2));

        List<Product> result = filterService.filterProducts(1, 50, 150, 4, List.of(1), List.of("Red"));

        assertThat(result).containsOnly(product1);
    }

    // Тест на фильтрацию с пустым списком товаров
    @Test
    void shouldReturnEmptyWhenNoProductsAvailable() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<Product> result = filterService.filterProducts(null, null, null, null, null, null);

        assertThat(result).isEmpty();
    }
}
