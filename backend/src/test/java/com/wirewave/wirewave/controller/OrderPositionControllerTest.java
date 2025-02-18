package com.wirewave.wirewave.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirewave.wirewave.config.JwtAuthenticationFilter;
import com.wirewave.wirewave.config.SecurityConfiguration;
import com.wirewave.wirewave.entity.OrderPosition;
import com.wirewave.wirewave.service.OrderPositionService;
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
@WebMvcTest(OrderPositionController.class)
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderPositionControllerTest {

    @MockBean
    private OrderPositionService orderPositionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderPosition orderPosition;

    @BeforeEach
    void setUp() {
        orderPosition = new OrderPosition();
        orderPosition.setId(1);
    }

    // Тест на получение всех позиций в заказах
    @Test
    void shouldGetAllOrderPositions() throws Exception {
        when(orderPositionService.getAllOrderPositions()).thenReturn(List.of(orderPosition));

        mockMvc.perform(get("/api/order-positions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(orderPosition.getId()));
    }

    // Тест на получение позиции в заказе по ID
    @Test
    void shouldGetOrderPositionById() throws Exception {
        when(orderPositionService.getOrderPositionById(1)).thenReturn(orderPosition);

        mockMvc.perform(get("/api/order-positions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderPosition.getId()));
    }

    // Тест на получение позиции по ID, если ее нет
    @Test
    void shouldReturnNotFoundIfOrderPositionNotExists() throws Exception {
        when(orderPositionService.getOrderPositionById(1)).thenReturn(null);

        mockMvc.perform(get("/api/order-positions/1"))
                .andExpect(status().isNotFound());
    }

    // Тест на создание позиции в заказе (только ADMIN)
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldCreateOrderPosition() throws Exception {
        when(orderPositionService.saveOrderPosition(any(OrderPosition.class))).thenReturn(orderPosition);

        mockMvc.perform(post("/api/order-positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderPosition)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderPosition.getId()));
    }

    // Тест на обновление позиции в заказе (только ADMIN)
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldUpdateOrderPosition() throws Exception {
        OrderPosition updatedOrderPosition = new OrderPosition();
        updatedOrderPosition.setId(1);

        when(orderPositionService.getOrderPositionById(1)).thenReturn(orderPosition);
        when(orderPositionService.saveOrderPosition(any(OrderPosition.class))).thenReturn(updatedOrderPosition);

        mockMvc.perform(put("/api/order-positions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrderPosition)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedOrderPosition.getId()));
    }

    // Тест на удаление позиции в заказе (только ADMIN)
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldDeleteOrderPosition() throws Exception {
        when(orderPositionService.getOrderPositionById(1)).thenReturn(orderPosition);
        Mockito.doNothing().when(orderPositionService).deleteOrderPosition(1);

        mockMvc.perform(delete("/api/order-positions/1"))
                .andExpect(status().isNoContent());
    }

    // Тест на удаление несуществующей позиции в заказе
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentOrderPosition() throws Exception {
        when(orderPositionService.getOrderPositionById(1)).thenReturn(null);

        mockMvc.perform(delete("/api/order-positions/1"))
                .andExpect(status().isNotFound());
    }
}
