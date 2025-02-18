package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Attribute;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.ProductAttribute;
import com.wirewave.wirewave.repository.AttributeRepository;
import com.wirewave.wirewave.repository.ProductAttributeRepository;
import com.wirewave.wirewave.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductAttributeRepository productAttributeRepository;

    @InjectMocks
    private AttributeService attributeService;

    private Attribute attribute;
    private Product product;
    private ProductAttribute productAttribute;

    @BeforeEach
    void setUp() {
        attribute = new Attribute();
        attribute.setId(1);
        attribute.setName("Color");
        attribute.setType("string");

        product = new Product();
        product.setId(1);
        product.setProductName("Test Product");

        productAttribute = new ProductAttribute();
        productAttribute.setId(1);
        productAttribute.setProduct(product);
        productAttribute.setAttribute(attribute);
        productAttribute.setValue("Red");
    }

    // Тест на создание характеристики
    @Test
    void shouldCreateAttribute() {
        when(attributeRepository.save(any(Attribute.class))).thenReturn(attribute);

        Attribute createdAttribute = attributeService.createAttribute(attribute);

        assertThat(createdAttribute).isNotNull();
        assertThat(createdAttribute.getId()).isEqualTo(attribute.getId());
        assertThat(createdAttribute.getName()).isEqualTo(attribute.getName());
        assertThat(createdAttribute.getType()).isEqualTo(attribute.getType());
    }

    // Тест на получение всех характеристик
    @Test
    void shouldReturnAllAttributes() {
        when(attributeRepository.findAll()).thenReturn(List.of(attribute));

        List<Attribute> attributes = attributeService.getAllAttributes();

        assertThat(attributes).hasSize(1);
        assertThat(attributes.get(0)).isEqualTo(attribute);
    }

    // Тест на привязку характеристики к товару
    @Test
    void shouldAssignAttributeToProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(attributeRepository.findById(1)).thenReturn(Optional.of(attribute));
        when(productAttributeRepository.save(any(ProductAttribute.class))).thenReturn(productAttribute);

        ProductAttribute assignedProductAttribute = attributeService.assignAttributeToProduct(1, 1, "Red");

        assertThat(assignedProductAttribute).isNotNull();
        assertThat(assignedProductAttribute.getProduct().getId()).isEqualTo(product.getId());
        assertThat(assignedProductAttribute.getAttribute().getId()).isEqualTo(attribute.getId());
        assertThat(assignedProductAttribute.getValue()).isEqualTo("Red");
    }

    // Тест на ошибку, если продукт не найден
    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> attributeService.assignAttributeToProduct(1, 1, "Red"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Product not found");
    }

    // Тест на ошибку, если характеристика не найдена
    @Test
    void shouldThrowExceptionWhenAttributeNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(attributeRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> attributeService.assignAttributeToProduct(1, 1, "Red"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Attribute not found");
    }
}
