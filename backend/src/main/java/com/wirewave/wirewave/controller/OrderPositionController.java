package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.entity.OrderPosition;
import com.wirewave.wirewave.service.OrderPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/order-positions")
public class OrderPositionController {

    @Autowired
    private OrderPositionService orderPositionService;

    // Создание новой позиции в заказе
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<OrderPosition> createOrderPosition(@Valid @RequestBody OrderPosition orderPosition) {
        OrderPosition savedOrderPosition = orderPositionService.saveOrderPosition(orderPosition);
        return ResponseEntity.ok(savedOrderPosition);
    }

    // Получение всех позиций в заказах
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderPosition>> getAllOrderPositions() {
        List<OrderPosition> orderPositions = orderPositionService.getAllOrderPositions();
        return ResponseEntity.ok(orderPositions);
    }

    // Получение позиции в заказе по ID
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderPosition> getOrderPositionById(@PathVariable Integer id) {
        OrderPosition orderPosition = orderPositionService.getOrderPositionById(id);
        return orderPosition != null ? ResponseEntity.ok(orderPosition) : ResponseEntity.notFound().build();
    }

    // Обновление данных позиции в заказе
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderPosition> updateOrderPosition(@PathVariable Integer id, @Valid @RequestBody OrderPosition orderPositionDetails) {
        OrderPosition orderPosition = orderPositionService.getOrderPositionById(id);
        if (orderPosition == null) {
            return ResponseEntity.notFound().build();
        }

        orderPosition.setProduct(orderPositionDetails.getProduct());
        orderPosition.setQuantity(orderPositionDetails.getQuantity());

        OrderPosition updatedOrderPosition = orderPositionService.saveOrderPosition(orderPosition);
        return ResponseEntity.ok(updatedOrderPosition);
    }

    // Удаление позиции в заказе по ID
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderPosition(@PathVariable Integer id) {
        if (orderPositionService.getOrderPositionById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        orderPositionService.deleteOrderPosition(id);
        return ResponseEntity.noContent().build();
    }
}
