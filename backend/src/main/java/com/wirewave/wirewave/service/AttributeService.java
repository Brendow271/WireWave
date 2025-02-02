package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Attribute;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.ProductAttribute;
import com.wirewave.wirewave.repository.AttributeRepository;
import com.wirewave.wirewave.repository.ProductAttributeRepository;
import com.wirewave.wirewave.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    // Создать характеристику
    public Attribute createAttribute(Attribute attribute) {
        return attributeRepository.save(attribute);
    }

    // Привязать характеристику к товару
    public ProductAttribute assignAttributeToProduct(Integer productId, Integer attributeId, String value) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setProduct(product);
        productAttribute.setAttribute(attribute);
        productAttribute.setValue(value);

        return productAttributeRepository.save(productAttribute);
    }

    // Получить все характеристики
    public List<Attribute> getAllAttributes() {
        return attributeRepository.findAll();
    }
}
