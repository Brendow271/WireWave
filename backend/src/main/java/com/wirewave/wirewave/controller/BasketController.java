package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.entity.Basket;
import com.wirewave.wirewave.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/baskets")
public class BasketController {

    @Autowired
    private BasketService basketService;

    // Создание новой корзины
    @PostMapping
    public ResponseEntity<Basket> createBasket(@Valid @RequestBody Basket basket) {
        Basket savedBasket = basketService.saveBasket(basket);
        return ResponseEntity.ok(savedBasket);
    }

    // Получение всех корзин
    @GetMapping
    public ResponseEntity<List<Basket>> getAllBaskets() {
        List<Basket> baskets = basketService.getAllBaskets();
        return ResponseEntity.ok(baskets);
    }

    // Получение корзины по ID
    @GetMapping("/{id}")
    public ResponseEntity<Basket> getBasketById(@PathVariable Integer id) {
        Basket basket = basketService.getBasketById(id);
        return basket != null ? ResponseEntity.ok(basket) : ResponseEntity.notFound().build();
    }

    // Обновление данных корзины
    @PutMapping("/{id}")
    public ResponseEntity<Basket> updateBasket(@PathVariable Integer id, @Valid @RequestBody Basket basketDetails) {
        Basket basket = basketService.getBasketById(id);
        if (basket == null) {
            return ResponseEntity.notFound().build();
        }

        basket.setUser(basketDetails.getUser());
        basket.setOrderPosition(basketDetails.getOrderPosition());
        basket.setTotalPrice(basketDetails.getTotalPrice());

        Basket updatedBasket = basketService.saveBasket(basket);
        return ResponseEntity.ok(updatedBasket);
    }

    // Удаление корзины по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBasket(@PathVariable Integer id) {
        if (basketService.getBasketById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        basketService.deleteBasket(id);
        return ResponseEntity.noContent().build();
    }
}
