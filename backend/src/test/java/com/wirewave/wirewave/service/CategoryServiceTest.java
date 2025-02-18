package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Category;
import com.wirewave.wirewave.repository.CategoryRepository;
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
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1);
        category.setCategoryName("Electronics");
    }

    // Тест на получение всех категорий
    @Test
    void shouldReturnAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> categories = categoryService.getAllCategories();

        assertThat(categories).hasSize(1);
        assertThat(categories.get(0)).isEqualTo(category);
    }

    // Тест на получение категории по ID
    @Test
    void shouldReturnCategoryById() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.getCategoryById(1);

        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getId()).isEqualTo(category.getId());
    }

    // Тест на получение категории по ID, если ее нет
    @Test
    void shouldReturnNullIfCategoryNotExists() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        Category foundCategory = categoryService.getCategoryById(1);

        assertThat(foundCategory).isNull();
    }

    // Тест на сохранение категории
    @Test
    void shouldSaveCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category savedCategory = categoryService.saveCategory(category);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isEqualTo(category.getId());
    }

    // Тест на удаление категории
    @Test
    void shouldDeleteCategory() {
        doNothing().when(categoryRepository).deleteById(1);

        categoryService.deleteCategory(1);

        verify(categoryRepository, times(1)).deleteById(1);
    }
}
