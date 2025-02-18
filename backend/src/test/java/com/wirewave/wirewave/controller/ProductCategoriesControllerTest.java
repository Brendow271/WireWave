package com.wirewave.wirewave.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirewave.wirewave.config.JwtAuthenticationFilter;
import com.wirewave.wirewave.config.JwtUtil;
import com.wirewave.wirewave.config.SecurityConfiguration;
import com.wirewave.wirewave.entity.ProductCategories;
import com.wirewave.wirewave.service.ProductCategoriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductCategoriesController.class)
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductCategoriesControllerTest {

    @MockBean
    private ProductCategoriesService productCategoriesService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtil jwtUtil;

    private ProductCategories productCategories;

    @BeforeEach
    void setUp() {
        productCategories = new ProductCategories();
        productCategories.setId(1);
    }

    // Тест на получение всех связей между продуктами и категориями
    @WithMockUser(roles = {"USER", "ADMIN"})
    @Test
    void shouldGetAllProductCategories() throws Exception {
        when(productCategoriesService.getAllProductCategories()).thenReturn(List.of(productCategories));

        mockMvc.perform(get("/api/product-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(productCategories.getId()));
    }

    // Тест на получение связи по ID
    @WithMockUser(roles = {"USER", "ADMIN"})
    @Test
    void shouldGetProductCategoryById() throws Exception {
        when(productCategoriesService.getProductCategoryById(1)).thenReturn(productCategories);

        mockMvc.perform(get("/api/product-categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productCategories.getId()));
    }

    // Тест на получение связи по ID, если она не найдена
    @WithMockUser(roles = {"USER", "ADMIN"})
    @Test
    void shouldReturnNotFoundIfProductCategoryNotExists() throws Exception {
        when(productCategoriesService.getProductCategoryById(1)).thenReturn(null);

        mockMvc.perform(get("/api/product-categories/1"))
                .andExpect(status().isNotFound());
    }

    // Тест на создание связи между продуктом и категорией (только ADMIN)
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldCreateProductCategory() throws Exception {
        when(productCategoriesService.saveProductCategory(any(ProductCategories.class))).thenReturn(productCategories);

        mockMvc.perform(post("/api/product-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCategories)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productCategories.getId()));
    }

    // Тест на обновление связи между продуктом и категорией (только ADMIN)
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldUpdateProductCategory() throws Exception {
        ProductCategories updatedProductCategory = new ProductCategories();
        updatedProductCategory.setId(1);

        when(productCategoriesService.getProductCategoryById(1)).thenReturn(productCategories);
        when(productCategoriesService.saveProductCategory(any(ProductCategories.class))).thenReturn(updatedProductCategory);

        mockMvc.perform(put("/api/product-categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProductCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedProductCategory.getId()));
    }

    // Тест на удаление связи между продуктом и категорией (только ADMIN)
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldDeleteProductCategory() throws Exception {
        when(productCategoriesService.getProductCategoryById(1)).thenReturn(productCategories);
        Mockito.doNothing().when(productCategoriesService).deleteProductCategory(1);

        mockMvc.perform(delete("/api/product-categories/1"))
                .andExpect(status().isNoContent());
    }

    // Тест на удаление несуществующей связи между продуктом и категорией
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentProductCategory() throws Exception {
        when(productCategoriesService.getProductCategoryById(1)).thenReturn(null);

        mockMvc.perform(delete("/api/product-categories/1"))
                .andExpect(status().isNotFound());
    }
}
