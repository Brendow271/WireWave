package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Basket;
import com.wirewave.wirewave.entity.OrderPosition;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.User;
import com.wirewave.wirewave.entity.Warehouse;
import com.wirewave.wirewave.repository.BasketRepository;
import com.wirewave.wirewave.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private BasketService basketService;

    private Basket basket;
    private User user;
    private Product product;
    private OrderPosition orderPosition;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setRole(User.Role.USER);

        product = new Product();
        product.setId(1);
        product.setProductName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));

        basket = new Basket();
        basket.setId(1);
        basket.setUser(user);

        orderPosition = new OrderPosition();
        orderPosition.setProduct(product);
        orderPosition.setQuantity(2);
        basket.getOrderPositions().add(orderPosition);
    }

    // Тест на получение корзины по ID пользователя
    @Test
    void shouldReturnBasketByUserId() {
        when(basketRepository.findByUserId(1)).thenReturn(basket);

        Basket foundBasket = basketService.getBasketByUserId(1);

        assertThat(foundBasket).isNotNull();
        assertThat(foundBasket.getUser().getId()).isEqualTo(user.getId());
    }

    // Тест на получение корзины по ID
    @Test
    void shouldReturnBasketById() {
        when(basketRepository.findById(1)).thenReturn(Optional.of(basket));

        Basket foundBasket = basketService.getBasketById(1);

        assertThat(foundBasket).isNotNull();
        assertThat(foundBasket.getId()).isEqualTo(basket.getId());
    }

    // Тест на удаление продукта из корзины
    @Test
    void shouldRemoveProductFromBasket() {
        when(basketRepository.findById(1)).thenReturn(Optional.of(basket));
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        Basket updatedBasket = basketService.removeProduct(1, 1);

        assertThat(updatedBasket).isNotNull();
        assertThat(updatedBasket.getOrderPositions()).isEmpty();
    }

    // Тест на обновление количества товаров в корзине
    @Test
    void shouldUpdateProductQuantityInBasket() {
        when(basketRepository.findById(1)).thenReturn(Optional.of(basket));
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        Basket updatedBasket = basketService.updateProductQuantity(1, 1, 5);

        assertThat(updatedBasket).isNotNull();
        assertThat(updatedBasket.getOrderPositions().get(0).getQuantity()).isEqualTo(5);
    }

    // Тест на удаление товара при установке количества в 0
    @Test
    void shouldRemoveProductWhenQuantityIsZero() {
        when(basketRepository.findById(1)).thenReturn(Optional.of(basket));
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        Basket updatedBasket = basketService.updateProductQuantity(1, 1, 0);

        assertThat(updatedBasket).isNotNull();
        assertThat(updatedBasket.getOrderPositions()).isEmpty();
    }

    // Тест на ошибку оформления заказа (товара нет в наличии)
    @Test
    void shouldFailCheckoutIfProductOutOfStock() {
        Warehouse warehouse = new Warehouse();
        warehouse.setProduct(product);
        warehouse.setQuantity(1);

        when(basketRepository.findById(1)).thenReturn(Optional.of(basket));
        when(warehouseRepository.findByProduct(product)).thenReturn(warehouse);

        boolean success = basketService.checkoutBasket(1);

        assertThat(success).isFalse();
        assertThat(warehouse.getQuantity()).isEqualTo(1);
    }

    // Тест на очистку корзины
    @Test
    void shouldClearBasket() {
        when(basketRepository.findById(1)).thenReturn(Optional.of(basket));
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        boolean cleared = basketService.clearBasket(1);

        assertThat(cleared).isTrue();
        assertThat(basket.getOrderPositions()).isEmpty();
    }

    // Тест на очистку несуществующей корзины
    @Test
    void shouldReturnFalseWhenClearingNonexistentBasket() {
        when(basketRepository.findById(1)).thenReturn(Optional.empty());

        boolean cleared = basketService.clearBasket(1);

        assertThat(cleared).isFalse();
    }
}
