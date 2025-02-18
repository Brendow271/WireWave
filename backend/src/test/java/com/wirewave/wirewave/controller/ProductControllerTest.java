package com.wirewave.wirewave.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirewave.wirewave.config.SecurityConfiguration;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.service.FilterService;
import com.wirewave.wirewave.service.ProductService;
import com.wirewave.wirewave.service.WarehouseService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private WarehouseService warehouseService;

    @MockBean
    private FilterService filterService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setProductName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));
    }

    // Тест на получение всех продуктов
    @Test
    void shouldGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].productName").value(product.getProductName()));
    }

    // Тест на получение продукта по ID
    @Test
    void shouldGetProductById() throws Exception {
        when(productService.getProductById(1)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.productName").value(product.getProductName()));
    }

    // Тест на получение продукта по ID, если он не найден
    @Test
    void shouldReturnNotFoundIfProductNotExists() throws Exception {
        when(productService.getProductById(1)).thenReturn(null);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    // Тест на создание продукта (только ADMIN)
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldCreateProduct() throws Exception {
        when(productService.saveProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .param("initialQuantity", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value(product.getProductName()));
    }

    // Тест на обновление продукта (только ADMIN)
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldUpdateProduct() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setId(1);
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setPrice(BigDecimal.valueOf(150));

        when(productService.getProductById(1)).thenReturn(product);
        when(productService.saveProduct(any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Updated Product"));
    }

    // Тест на удаление продукта (только ADMIN)
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldDeleteProduct() throws Exception {
        when(productService.getProductById(1)).thenReturn(product);
        Mockito.doNothing().when(productService).deleteProduct(1);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    // Тест на получение средней оценки продукта
    @Test
    void shouldGetProductRating() throws Exception {
        Product mockProduct = Mockito.spy(product);
        when(productService.getProductById(1)).thenReturn(mockProduct);
        when(mockProduct.getAverageRating()).thenReturn(4.5);

        mockMvc.perform(get("/api/products/1/rating"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.5"));
    }


    // Тест на получение количества товара на складе
    @Test
    void shouldGetProductStock() throws Exception {
        when(productService.getProductById(1)).thenReturn(product);
        when(warehouseService.getTotalStockForProduct(product)).thenReturn(20);

        mockMvc.perform(get("/api/products/1/stock"))
                .andExpect(status().isOk())
                .andExpect(content().string("20"));
    }

    // Тест на фильтрацию продуктов
    @Test
    void shouldFilterProducts() throws Exception {
        when(filterService.filterProducts(null, null, null, null, null, null)).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].productName").value(product.getProductName()));
    }
}
