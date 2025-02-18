package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.ProductCategories;
import com.wirewave.wirewave.repository.ProductCategoriesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCategoriesServiceTest {

    @Mock
    private ProductCategoriesRepository productCategoriesRepository;

    @InjectMocks
    private ProductCategoriesService productCategoriesService;

    private ProductCategories productCategories;

    @BeforeEach
    void setUp() {
        productCategories = new ProductCategories();
        productCategories.setId(1);
    }

    // Тест на получение всех связей между продуктами и категориями
    @Test
    void shouldReturnAllProductCategories() {
        when(productCategoriesRepository.findAll()).thenReturn(List.of(productCategories));

        List<ProductCategories> result = productCategoriesService.getAllProductCategories();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(productCategories);
    }

    // Тест на получение связи между продуктом и категорией по ID
    @Test
    void shouldReturnProductCategoryById() {
        when(productCategoriesRepository.findById(1)).thenReturn(Optional.of(productCategories));

        ProductCategories result = productCategoriesService.getProductCategoryById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productCategories.getId());
    }

    // Тест на получение связи между продуктом и категорией по ID, если она не найдена
    @Test
    void shouldReturnNullIfProductCategoryNotExists() {
        when(productCategoriesRepository.findById(1)).thenReturn(Optional.empty());

        ProductCategories result = productCategoriesService.getProductCategoryById(1);

        assertThat(result).isNull();
    }

    // Тест на сохранение связи между продуктом и категорией
    @Test
    void shouldSaveProductCategory() {
        when(productCategoriesRepository.save(any(ProductCategories.class))).thenReturn(productCategories);

        ProductCategories result = productCategoriesService.saveProductCategory(productCategories);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productCategories.getId());
    }

    // Тест на удаление связи между продуктом и категорией
    @Test
    void shouldDeleteProductCategory() {
        doNothing().when(productCategoriesRepository).deleteById(1);

        productCategoriesService.deleteProductCategory(1);

        verify(productCategoriesRepository, times(1)).deleteById(1);
    }
}
