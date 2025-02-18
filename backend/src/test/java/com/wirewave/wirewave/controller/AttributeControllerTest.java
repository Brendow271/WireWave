package com.wirewave.wirewave.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirewave.wirewave.config.SecurityConfiguration;
import com.wirewave.wirewave.entity.Attribute;
import com.wirewave.wirewave.entity.ProductAttribute;
import com.wirewave.wirewave.service.AttributeService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AttributeController.class)
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class AttributeControllerTest {

    @MockBean
    private AttributeService attributeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private Attribute attribute;
    private ProductAttribute productAttribute;

    @BeforeEach
    void setUp() {
        attribute = new Attribute();
        attribute.setId(1);
        attribute.setName("Color");
        attribute.setType("string");

        productAttribute = new ProductAttribute();
        productAttribute.setId(1);
        productAttribute.setValue("Red");
    }

    // Тест на создание характеристики
    @Test
    void shouldCreateAttribute() throws Exception {
        Mockito.when(attributeService.createAttribute(any(Attribute.class))).thenReturn(attribute);

        mockMvc.perform(post("/api/attributes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attribute)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(attribute.getId()))
                .andExpect(jsonPath("$.name").value(attribute.getName()))
                .andExpect(jsonPath("$.type").value(attribute.getType()));
    }

    // Тест на получение всех характеристик
    @Test
    void shouldGetAllAttributes() throws Exception {
        List<Attribute> attributes = Arrays.asList(attribute);
        Mockito.when(attributeService.getAllAttributes()).thenReturn(attributes);

        mockMvc.perform(get("/api/attributes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(attribute.getId()))
                .andExpect(jsonPath("$[0].name").value(attribute.getName()))
                .andExpect(jsonPath("$[0].type").value(attribute.getType()));
    }

    // Тест на привязку атрибута к продукту
    @Test
    void shouldAssignAttributeToProduct() throws Exception {
        Mockito.when(attributeService.assignAttributeToProduct(eq(1), eq(1), eq("Red"))).thenReturn(productAttribute);

        mockMvc.perform(post("/api/attributes/assign")
                        .param("productId", "1")
                        .param("attributeId", "1")
                        .param("value", "Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productAttribute.getId()))
                .andExpect(jsonPath("$.value").value(productAttribute.getValue()));
    }

    // Тест на обработку ошибки, если атрибут не найден
    @Test
    void shouldReturnErrorWhenAttributeNotFound() throws Exception {
        Mockito.when(attributeService.createAttribute(any(Attribute.class)))
                .thenThrow(new RuntimeException("Attribute not found"));

        mockMvc.perform(post("/api/attributes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attribute)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Attribute not found"));
    }

    // Тест на создание атрибута с ролью ADMIN
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldAllowAdminToCreateAttribute() throws Exception {
        Mockito.when(attributeService.createAttribute(any(Attribute.class))).thenReturn(attribute);

        mockMvc.perform(post("/api/attributes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attribute)))
                .andExpect(status().isOk());
    }

    // Тест на привязку атрибута к продукту с ролью ADMIN
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldAllowAdminToAssignAttributeToProduct() throws Exception {
        Mockito.when(attributeService.assignAttributeToProduct(eq(1), eq(1), eq("Red"))).thenReturn(productAttribute);

        mockMvc.perform(post("/api/attributes/assign")
                        .param("productId", "1")
                        .param("attributeId", "1")
                        .param("value", "Red"))
                .andExpect(status().isOk());
    }

    // Тест на пустой запрос при создании атрибута
    @Test
    void shouldReturnBadRequestForEmptyAttribute() throws Exception {
        mockMvc.perform(post("/api/attributes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

}

