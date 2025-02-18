package com.wirewave.wirewave.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirewave.wirewave.config.JwtAuthenticationFilter;
import com.wirewave.wirewave.config.SecurityConfiguration;
import com.wirewave.wirewave.entity.Warehouse;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@ExtendWith(SpringExtension.class)
@WebMvcTest(WarehouseController.class)
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class WarehouseControllerTest {

    @MockBean
    private WarehouseService warehouseService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        warehouse.setId(1);
        warehouse.setQuantity(100);
    }

    // Тест на создание склада
    @Test
    void shouldCreateWarehouse() throws Exception {
        Mockito.when(warehouseService.getAllWarehouses()).thenReturn(Collections.emptyList());
        Mockito.when(warehouseService.saveWarehouse(any(Warehouse.class))).thenReturn(warehouse);

        mockMvc.perform(post("/api/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(warehouse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(warehouse.getId()))
                .andExpect(jsonPath("$.quantity").value(warehouse.getQuantity()));
    }

    // Тест на ошибку при попытке создать второй склад
    @Test
    void shouldNotCreateSecondWarehouse() throws Exception {
        Mockito.when(warehouseService.getAllWarehouses()).thenReturn(List.of(warehouse));

        mockMvc.perform(post("/api/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(warehouse)))
                .andExpect(status().isBadRequest());
    }

    // Тест на получение всех складов
    @Test
    void shouldGetAllWarehouses() throws Exception {
        Mockito.when(warehouseService.getAllWarehouses()).thenReturn(List.of(warehouse));

        mockMvc.perform(get("/api/warehouses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(warehouse.getId()))
                .andExpect(jsonPath("$[0].quantity").value(warehouse.getQuantity()));
    }

    // Тест на получение склада по ID
    @Test
    void shouldGetWarehouseById() throws Exception {
        Mockito.when(warehouseService.getWarehouseById(1)).thenReturn(warehouse);

        mockMvc.perform(get("/api/warehouses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(warehouse.getId()))
                .andExpect(jsonPath("$.quantity").value(warehouse.getQuantity()));
    }

    // Тест на ошибку, если склад не найден
    @Test
    void shouldReturnNotFoundWhenWarehouseNotExists() throws Exception {
        Mockito.when(warehouseService.getWarehouseById(1)).thenReturn(null);

        mockMvc.perform(get("/api/warehouses/1"))
                .andExpect(status().isNotFound());
    }

    // Тест на обновление склада
    @Test
    void shouldUpdateWarehouse() throws Exception {
        Warehouse updatedWarehouse = new Warehouse();
        updatedWarehouse.setId(1);
        updatedWarehouse.setQuantity(200);

        Mockito.when(warehouseService.getWarehouseById(1)).thenReturn(warehouse);
        Mockito.when(warehouseService.saveWarehouse(any(Warehouse.class))).thenReturn(updatedWarehouse);

        mockMvc.perform(put("/api/warehouses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedWarehouse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedWarehouse.getId()))
                .andExpect(jsonPath("$.quantity").value(updatedWarehouse.getQuantity()));
    }

    // Тест на ошибку при обновлении склада, если он не найден
    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentWarehouse() throws Exception {
        Mockito.when(warehouseService.getWarehouseById(1)).thenReturn(null);

        mockMvc.perform(put("/api/warehouses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(warehouse)))
                .andExpect(status().isNotFound());
    }

    // Тест на проверку наличия товара
    @Test
    void shouldReturnProductAvailability() throws Exception {
        Mockito.when(warehouseService.getProductAvailabilityStatus(1)).thenReturn("Available");

        mockMvc.perform(get("/api/warehouses/product/1/availability"))
                .andExpect(status().isOk())
                .andExpect(content().string("Available"));
    }

    // Тест на ошибку, если товара нет
    @Test
    void shouldReturnNotFoundWhenProductNotAvailable() throws Exception {
        Mockito.when(warehouseService.getProductAvailabilityStatus(1)).thenReturn(null);

        mockMvc.perform(get("/api/warehouses/product/1/availability"))
                .andExpect(status().isNotFound());
    }

    // Тест на удаление склада
    @Test
    void shouldDeleteWarehouse() throws Exception {
        Mockito.when(warehouseService.getWarehouseById(1)).thenReturn(warehouse);
        Mockito.doNothing().when(warehouseService).deleteWarehouse(1);

        mockMvc.perform(delete("/api/warehouses/1"))
                .andExpect(status().isNoContent());
    }

    // Тест на ошибку при удалении несуществующего склада
    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentWarehouse() throws Exception {
        Mockito.when(warehouseService.getWarehouseById(1)).thenReturn(null);

        mockMvc.perform(delete("/api/warehouses/1"))
                .andExpect(status().isNotFound());
    }
}
