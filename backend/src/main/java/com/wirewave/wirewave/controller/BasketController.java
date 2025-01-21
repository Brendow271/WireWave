package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.entity.Basket;
import com.wirewave.wirewave.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/baskets")
public class BasketController {

    @Autowired
    private BasketService basketService;

    // Получение корзины пользователя по ID пользователя
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Basket> getBasketByUserId(@PathVariable Integer userId) {
        Basket basket = basketService.getBasketByUserId(userId);
        return basket != null ? ResponseEntity.ok(basket) : ResponseEntity.notFound().build();
    }

    // Добавление продукта в корзину
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{basketId}/add-product")
    public ResponseEntity<Basket> addProductToBasket(@PathVariable Integer basketId,
                                                     @RequestParam Integer productId,
                                                     @RequestParam Integer quantity) {
        Basket updatedBasket = basketService.addProduct(basketId, productId, quantity);
        return updatedBasket != null ? ResponseEntity.ok(updatedBasket) : ResponseEntity.notFound().build();
    }

    // Удаление продукта из корзины
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{basketId}/remove-product")
    public ResponseEntity<Basket> removeProductFromBasket(@PathVariable Integer basketId,
                                                          @RequestParam Integer productId) {
        Basket updatedBasket = basketService.removeProduct(basketId, productId);
        return updatedBasket != null ? ResponseEntity.ok(updatedBasket) : ResponseEntity.notFound().build();
    }

    // Возвращать общую стоимость корзины
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{basketId}/total-price")
    public ResponseEntity<BigDecimal> getBasketTotalPrice(@PathVariable Integer basketId) {
        Basket basket = basketService.getBasketById(basketId);
        if (basket == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(basket.getTotalPrice());
    }

    // Кнопки +/- для изменения количества товаров
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{basketId}/update-quantity")
    public ResponseEntity<Basket> updateProductQuantity(@PathVariable Integer basketId,
                                                        @RequestParam Integer productId,
                                                        @RequestParam Integer quantity) {
        Basket updatedBasket = basketService.updateProductQuantity(basketId, productId, quantity);
        return updatedBasket != null ? ResponseEntity.ok(updatedBasket) : ResponseEntity.notFound().build();
    }

    // Метод для оформления заказа на основе корзины
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{basketId}/checkout")
    public ResponseEntity<String> checkout(@PathVariable Integer basketId) {
        boolean success = basketService.checkoutBasket(basketId);
        return success ? ResponseEntity.ok("Order placed successfully") : ResponseEntity.badRequest().body("Checkout failed");
    }

    // Очистка корзины
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{basketId}/clear")
    public ResponseEntity<Void> clearBasket(@PathVariable Integer basketId) {
        boolean cleared = basketService.clearBasket(basketId);
        return cleared ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
