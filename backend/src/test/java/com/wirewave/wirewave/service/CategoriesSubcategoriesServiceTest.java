package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.CategoriesSubcategories;
import com.wirewave.wirewave.repository.CategoriesSubcategoriesRepository;
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
class CategoriesSubcategoriesServiceTest {

    @Mock
    private CategoriesSubcategoriesRepository categoriesSubcategoriesRepository;

    @InjectMocks
    private CategoriesSubcategoriesService categoriesSubcategoriesService;

    private CategoriesSubcategories categoriesSubcategories;

    @BeforeEach
    void setUp() {
        categoriesSubcategories = new CategoriesSubcategories();
        categoriesSubcategories.setId(1);
    }

    // Тест на получение всех связей между категориями и подкатегориями
    @Test
    void shouldReturnAllCategoriesSubcategories() {
        when(categoriesSubcategoriesRepository.findAll()).thenReturn(List.of(categoriesSubcategories));

        List<CategoriesSubcategories> result = categoriesSubcategoriesService.getAllCategoriesSubcategories();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(categoriesSubcategories);
    }

    // Тест на получение связи по ID
    @Test
    void shouldReturnCategoriesSubcategoriesById() {
        when(categoriesSubcategoriesRepository.findById(1)).thenReturn(Optional.of(categoriesSubcategories));

        CategoriesSubcategories result = categoriesSubcategoriesService.getCategoriesSubcategoriesById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(categoriesSubcategories.getId());
    }

    // Тест на получение связи по ID, если ее нет
    @Test
    void shouldReturnNullIfCategoriesSubcategoriesNotExists() {
        when(categoriesSubcategoriesRepository.findById(1)).thenReturn(Optional.empty());

        CategoriesSubcategories result = categoriesSubcategoriesService.getCategoriesSubcategoriesById(1);

        assertThat(result).isNull();
    }

    // Тест на сохранение связи
    @Test
    void shouldSaveCategoriesSubcategories() {
        when(categoriesSubcategoriesRepository.save(any(CategoriesSubcategories.class))).thenReturn(categoriesSubcategories);

        CategoriesSubcategories result = categoriesSubcategoriesService.saveCategoriesSubcategories(categoriesSubcategories);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(categoriesSubcategories.getId());
    }

    // Тест на удаление связи
    @Test
    void shouldDeleteCategoriesSubcategories() {
        doNothing().when(categoriesSubcategoriesRepository).deleteById(1);

        categoriesSubcategoriesService.deleteCategoriesSubcategories(1);

        verify(categoriesSubcategoriesRepository, times(1)).deleteById(1);
    }
}
