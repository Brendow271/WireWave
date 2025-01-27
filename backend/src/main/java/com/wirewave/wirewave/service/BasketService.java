package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Basket;
import com.wirewave.wirewave.entity.OrderPosition;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.Warehouse;
import com.wirewave.wirewave.repository.BasketRepository;
import com.wirewave.wirewave.repository.OrderPositionRepository;
import com.wirewave.wirewave.repository.ProductRepository;
import com.wirewave.wirewave.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderPositionRepository orderPositionRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private EmailService emailService;

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

    // Кнопки +/- для изменения количества товаров
    public Basket updateProductQuantity(Integer basketId, Integer productId, Integer quantity) {
        Optional<Basket> optionalBasket = basketRepository.findById(basketId);
        if (optionalBasket.isEmpty()) {
            return null;
        }

        Basket basket = optionalBasket.get();

        Optional<OrderPosition> positionOptional = basket.getOrderPositions().stream()
                .filter(op -> op.getProduct().getId().equals(productId))
                .findFirst();

        if (positionOptional.isEmpty() || quantity <= 0) {
            basket.getOrderPositions().removeIf(op -> op.getProduct().getId().equals(productId));
        } else {
            OrderPosition position = positionOptional.get();
            position.setQuantity(quantity);
        }

        return basketRepository.save(basket);
    }

    // Метод для проверки наличия товаров, уменьшения остатков, очистки корзины и отправки подтверждающего Email
    public boolean checkoutBasket(Integer basketId) {
        Optional<Basket> optionalBasket = basketRepository.findById(basketId);
        if (optionalBasket.isEmpty()) {
            return false;
        }

        Basket basket = optionalBasket.get();

        for (OrderPosition position : basket.getOrderPositions()) {
            Product product = position.getProduct();
            int quantity = position.getQuantity();

            Warehouse warehouse = warehouseRepository.findByProduct(product);
            if (warehouse == null || warehouse.getQuantity() < quantity) {
                return false;
            }

            warehouse.setQuantity(warehouse.getQuantity() - quantity);
            warehouseRepository.save(warehouse);
        }

        basket.getOrderPositions().clear();
        basketRepository.save(basket);

        // Формируем детали заказа
        String recipientEmail = basket.getUser().getEmail();
        String subject = "Ваш заказ успешно оформлен";
        StringBuilder body = new StringBuilder();
        body.append("Здравствуйте, ").append(basket.getUser().getFirstName()).append("!\n\n");
        body.append("Спасибо за ваш заказ. Вот его детали:\n");
        for (OrderPosition position : basket.getOrderPositions()) {
            body.append("- ").append(position.getProduct().getProductName())
                    .append(" x ").append(position.getQuantity())
                    .append(" = ").append(position.getProduct().getPrice().multiply(BigDecimal.valueOf(position.getQuantity())))
                    .append(" RUB\n");
        }
        body.append("\nОбщая сумма: ").append(basket.getTotalPrice()).append(" RUB\n");
        body.append("\nС уважением,\nКоманда WireWave");

        emailService.sendOrderEmail(recipientEmail, subject, body.toString());
        basket.getOrderPositions().clear();
        basketRepository.save(basket);


        return true;
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
