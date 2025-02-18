//// Тесты для CategoryController
//package com.wirewave.wirewave.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.wirewave.wirewave.config.JwtAuthenticationFilter;
//import com.wirewave.wirewave.config.SecurityConfiguration;
//import com.wirewave.wirewave.entity.Category;
//import com.wirewave.wirewave.service.CategoryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(CategoryController.class)
//@Import(SecurityConfiguration.class)
//@AutoConfigureMockMvc(addFilters = false)
//class CategoryControllerTest {
//
//    @MockBean
//    private CategoryService categoryService;
//
//    @MockBean
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Category category;
//
//    @BeforeEach
//    void setUp() {
//        category = new Category();
//        category.setId(1);
//        category.setCategoryName("Electronics");
//    }
//
//    // Тест на получение всех категорий
//    @Test
//    void shouldGetAllCategories() throws Exception {
//        when(categoryService.getAllCategories()).thenReturn(List.of(category));
//
//        mockMvc.perform(get("/api/categories"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(1))
//                .andExpect(jsonPath("$[0].categoryName").value(category.getCategoryName()));
//    }
//
//    // Тест на получение категории по ID
//    @Test
//    void shouldGetCategoryById() throws Exception {
//        when(categoryService.getCategoryById(1)).thenReturn(category);
//
//        mockMvc.perform(get("/api/categories/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(category.getId()))
//                .andExpect(jsonPath("$.categoryName").value(category.getCategoryName()));
//    }
//
//    // Тест на получение категории по ID, если она не найдена
//    @Test
//    void shouldReturnNotFoundIfCategoryNotExists() throws Exception {
//        when(categoryService.getCategoryById(1)).thenReturn(null);
//
//        mockMvc.perform(get("/api/categories/1"))
//                .andExpect(status().isNotFound());
//    }
//
//    // Тест на создание категории (только ADMIN)
//    @WithMockUser(roles = "ADMIN")
//    @Test
//    void shouldCreateCategory() throws Exception {
//        when(categoryService.saveCategory(any(Category.class))).thenReturn(category);
//
//        mockMvc.perform(post("/api/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(category)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.categoryName").value(category.getCategoryName()));
//    }
//
//    // Тест на обновление категории (только ADMIN)
//    @WithMockUser(roles = "ADMIN")
//    @Test
//    void shouldUpdateCategory() throws Exception {
//        Category updatedCategory = new Category();
//        updatedCategory.setId(1);
//        updatedCategory.setCategoryName("Updated Electronics");
//
//        when(categoryService.getCategoryById(1)).thenReturn(category);
//        when(categoryService.saveCategory(any(Category.class))).thenReturn(updatedCategory);
//
//        mockMvc.perform(put("/api/categories/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedCategory)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.categoryName").value("Updated Electronics"));
//    }
//
//    // Тест на удаление категории (только ADMIN)
//    @WithMockUser(roles = "ADMIN")
//    @Test
//    void shouldDeleteCategory() throws Exception {
//        when(categoryService.getCategoryById(1)).thenReturn(category);
//        Mockito.doNothing().when(categoryService).deleteCategory(1);
//
//        mockMvc.perform(delete("/api/categories/1"))
//                .andExpect(status().isNoContent());
//    }
//
//    // Тест на удаление несуществующей категории
//    @WithMockUser(roles = "ADMIN")
//    @Test
//    void shouldReturnNotFoundWhenDeletingNonexistentCategory() throws Exception {
//        when(categoryService.getCategoryById(1)).thenReturn(null);
//
//        mockMvc.perform(delete("/api/categories/1"))
//                .andExpect(status().isNotFound());
//    }
//}
