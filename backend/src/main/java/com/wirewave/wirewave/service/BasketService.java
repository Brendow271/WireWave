package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Basket;
import com.wirewave.wirewave.entity.OrderPosition;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.repository.BasketRepository;
import com.wirewave.wirewave.repository.OrderPositionRepository;
import com.wirewave.wirewave.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderPositionRepository orderPositionRepository;

    // Получение корзины по ID пользователя
    public Basket getBasketByUserId(Integer userId) {
        return basketRepository.findByUserId(userId);
    }

    // Получение корзины по ID корзины
    public Basket getBasketById(Integer basketId) {
        return basketRepository.findById(basketId).orElse(null);
    }

    // Добавление продукта в корзину
    public Basket addProduct(Integer basketId, Integer productId, Integer quantity) {
        Optional<Basket> optionalBasket = basketRepository.findById(basketId);
        if (optionalBasket.isEmpty()) {
            return null;
        }

        Basket basket = optionalBasket.get();

        // Проверка существования продукта
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return null;
        }

        Product product = optionalProduct.get();

        // Проверяем, есть ли уже такая позиция в корзине
        Optional<OrderPosition> existingPosition = basket.getOrderPositions().stream()
                .filter(op -> op.getProduct().getId().equals(productId))
                .findFirst();

        if (existingPosition.isPresent()) {
            // Если позиция уже есть, увеличиваем количество
            OrderPosition orderPosition = existingPosition.get();
            orderPosition.setQuantity(orderPosition.getQuantity() + quantity);
        } else {
            // Если позиции нет, создаём новую
            OrderPosition newPosition = new OrderPosition();
            newPosition.setProduct(product);
            newPosition.setQuantity(quantity);
            newPosition.setBasket(basket);
            basket.getOrderPositions().add(newPosition);
        }

        return basketRepository.save(basket);
    }

    // Удаление продукта из корзины
    public Basket removeProduct(Integer basketId, Integer productId) {
        Optional<Basket> optionalBasket = basketRepository.findById(basketId);
        if (optionalBasket.isEmpty()) {
            return null;
        }

        Basket basket = optionalBasket.get();

        // Удаляем позицию заказа с указанным продуктом
        basket.getOrderPositions().removeIf(op -> op.getProduct().getId().equals(productId));

        return basketRepository.save(basket);
    }

    // Очистка корзины
    public boolean clearBasket(Integer basketId) {
        Optional<Basket> optionalBasket = basketRepository.findById(basketId);
        if (optionalBasket.isEmpty()) {
            return false;
        }

        Basket basket = optionalBasket.get();
        basket.getOrderPositions().clear();
        basketRepository.save(basket);
        return true;
    }
}
