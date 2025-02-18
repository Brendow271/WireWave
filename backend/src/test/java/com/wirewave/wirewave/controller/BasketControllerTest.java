package com.wirewave.wirewave.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirewave.wirewave.config.JwtAuthenticationFilter;
import com.wirewave.wirewave.config.JwtUtil;
import com.wirewave.wirewave.config.SecurityConfiguration;
import com.wirewave.wirewave.entity.Basket;
import com.wirewave.wirewave.service.BasketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BasketController.class)
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class BasketControllerTest {

    @MockBean
    private BasketService basketService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtil jwtUtil;

    private Basket basket;

    @BeforeEach
    void setUp() {
        basket = new Basket();
        basket.setId(1);
    }

    // Тест на получение корзины пользователя по ID
    @WithMockUser(roles = "USER")
    @Test
    void shouldGetBasketByUserId() throws Exception {
        Mockito.when(basketService.getBasketByUserId(1)).thenReturn(basket);

        mockMvc.perform(get("/api/baskets/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(basket.getId()));
    }

    // Тест на получение корзины, если пользователя нет
    @WithMockUser(roles = "USER")
    @Test
    void shouldReturnNotFoundIfBasketNotExists() throws Exception {
        Mockito.when(basketService.getBasketByUserId(1)).thenReturn(null);

        mockMvc.perform(get("/api/baskets/user/1"))
                .andExpect(status().isNotFound());
    }

    // Тест на добавление продукта в корзину
    @WithMockUser(roles = "USER")
    @Test
    void shouldAddProductToBasket() throws Exception {
        Mockito.when(basketService.addProduct(1, 1, 2)).thenReturn(basket);

        mockMvc.perform(post("/api/baskets/1/add-product")
                        .param("productId", "1")
                        .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(basket.getId()));
    }

    // Тест на удаление продукта из корзины
    @WithMockUser(roles = "USER")
    @Test
    void shouldRemoveProductFromBasket() throws Exception {
        Mockito.when(basketService.removeProduct(1, 1)).thenReturn(basket);

        mockMvc.perform(delete("/api/baskets/1/remove-product")
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(basket.getId()));
    }

    // Тест на обновление количества товара в корзине
    @WithMockUser(roles = "USER")
    @Test
    void shouldUpdateProductQuantity() throws Exception {
        Mockito.when(basketService.updateProductQuantity(1, 1, 5)).thenReturn(basket);

        mockMvc.perform(put("/api/baskets/1/update-quantity")
                        .param("productId", "1")
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(basket.getId()));
    }

    // Тест на оформление заказа
    @WithMockUser(roles = "USER")
    @Test
    void shouldCheckoutBasketSuccessfully() throws Exception {
        Mockito.when(basketService.checkoutBasket(1)).thenReturn(true);

        mockMvc.perform(post("/api/baskets/1/checkout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order placed successfully"));
    }

    // Тест на неудачное оформление заказа
    @WithMockUser(roles = "USER")
    @Test
    void shouldFailCheckoutIfNotEnoughStock() throws Exception {
        Mockito.when(basketService.checkoutBasket(1)).thenReturn(false);

        mockMvc.perform(post("/api/baskets/1/checkout"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Checkout failed"));
    }

    // Тест на очистку корзины
    @WithMockUser(roles = "USER")
    @Test
    void shouldClearBasket() throws Exception {
        Mockito.when(basketService.clearBasket(1)).thenReturn(true);

        mockMvc.perform(delete("/api/baskets/1/clear"))
                .andExpect(status().isNoContent());
    }

    // Тест на очистку несуществующей корзины
    @WithMockUser(roles = "USER")
    @Test
    void shouldReturnNotFoundWhenClearingNonexistentBasket() throws Exception {
        Mockito.when(basketService.clearBasket(1)).thenReturn(false);

        mockMvc.perform(delete("/api/baskets/1/clear"))
                .andExpect(status().isNotFound());
    }
}
