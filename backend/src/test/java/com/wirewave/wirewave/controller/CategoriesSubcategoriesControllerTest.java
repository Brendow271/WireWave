package com.wirewave.wirewave.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirewave.wirewave.config.JwtAuthenticationFilter;
import com.wirewave.wirewave.config.JwtUtil;
import com.wirewave.wirewave.config.SecurityConfiguration;
import com.wirewave.wirewave.entity.CategoriesSubcategories;
import com.wirewave.wirewave.service.CategoriesSubcategoriesService;
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
@WebMvcTest(CategoriesSubcategoriesController.class)
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoriesSubcategoriesControllerTest {

    @MockBean
    private CategoriesSubcategoriesService categoriesSubcategoriesService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtil jwtUtil;

    private CategoriesSubcategories categoriesSubcategories;

    @BeforeEach
    void setUp() {
        categoriesSubcategories = new CategoriesSubcategories();
        categoriesSubcategories.setId(1);
    }

    // Тест на получение всех связей между категориями и подкатегориями
    @Test
    void shouldGetAllCategoriesSubcategories() throws Exception {
        when(categoriesSubcategoriesService.getAllCategoriesSubcategories()).thenReturn(List.of(categoriesSubcategories));

        mockMvc.perform(get("/api/categories-subcategories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(categoriesSubcategories.getId()));
    }

    // Тест на получение связи по ID
    @Test
    void shouldGetCategoriesSubcategoriesById() throws Exception {
        when(categoriesSubcategoriesService.getCategoriesSubcategoriesById(1)).thenReturn(categoriesSubcategories);

        mockMvc.perform(get("/api/categories-subcategories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoriesSubcategories.getId()));
    }

    // Тест на получение связи по ID, если ее нет
    @Test
    void shouldReturnNotFoundIfCategoriesSubcategoriesNotExists() throws Exception {
        when(categoriesSubcategoriesService.getCategoriesSubcategoriesById(1)).thenReturn(null);

        mockMvc.perform(get("/api/categories-subcategories/1"))
                .andExpect(status().isNotFound());
    }

    // Тест на создание связи (только ADMIN)
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldCreateCategoriesSubcategories() throws Exception {
        when(categoriesSubcategoriesService.saveCategoriesSubcategories(any(CategoriesSubcategories.class))).thenReturn(categoriesSubcategories);

        mockMvc.perform(post("/api/categories-subcategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriesSubcategories)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoriesSubcategories.getId()));
    }

    // Тест на обновление связи (только ADMIN)
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldUpdateCategoriesSubcategories() throws Exception {
        CategoriesSubcategories updated = new CategoriesSubcategories();
        updated.setId(1);

        when(categoriesSubcategoriesService.getCategoriesSubcategoriesById(1)).thenReturn(categoriesSubcategories);
        when(categoriesSubcategoriesService.saveCategoriesSubcategories(any(CategoriesSubcategories.class))).thenReturn(updated);

        mockMvc.perform(put("/api/categories-subcategories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updated.getId()));
    }

    // Тест на удаление связи (только ADMIN)
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldDeleteCategoriesSubcategories() throws Exception {
        when(categoriesSubcategoriesService.getCategoriesSubcategoriesById(1)).thenReturn(categoriesSubcategories);
        Mockito.doNothing().when(categoriesSubcategoriesService).deleteCategoriesSubcategories(1);

        mockMvc.perform(delete("/api/categories-subcategories/1"))
                .andExpect(status().isNoContent());
    }

    // Тест на удаление несуществующей связи
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentCategoriesSubcategories() throws Exception {
        when(categoriesSubcategoriesService.getCategoriesSubcategoriesById(1)).thenReturn(null);

        mockMvc.perform(delete("/api/categories-subcategories/1"))
                .andExpect(status().isNotFound());
    }
}
