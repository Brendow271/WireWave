package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.entity.Attribute;
import com.wirewave.wirewave.entity.ProductAttribute;
import com.wirewave.wirewave.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attributes")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    // Обработчик ошибок (если атрибут не найден или другие ошибки)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(500).body("{\"message\":\"" + ex.getMessage() + "\"}");
    }

    // Создать новую характеристику
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Attribute> createAttribute(@Valid @RequestBody Attribute attribute) {
        Attribute createdAttribute = attributeService.createAttribute(attribute);
        return ResponseEntity.ok(createdAttribute);
    }

    // Привязать характеристику к товару
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign")
    public ResponseEntity<ProductAttribute> assignAttributeToProduct(
            @RequestParam Integer productId,
            @RequestParam Integer attributeId,
            @RequestParam String value) {
        ProductAttribute productAttribute = attributeService.assignAttributeToProduct(productId, attributeId, value);
        return ResponseEntity.ok(productAttribute);
    }

    // Получить все характеристики
    @GetMapping
    public ResponseEntity<List<Attribute>> getAllAttributes() {
        List<Attribute> attributes = attributeService.getAllAttributes();
        return ResponseEntity.ok(attributes);
    }
}
