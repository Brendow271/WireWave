package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.entity.Attribute;
import com.wirewave.wirewave.entity.ProductAttribute;
import com.wirewave.wirewave.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attributes")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    // Создать новую характеристику
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Attribute> createAttribute(@RequestBody Attribute attribute) {
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
