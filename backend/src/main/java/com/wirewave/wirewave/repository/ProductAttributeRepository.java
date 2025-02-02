package com.wirewave.wirewave.repository;

import com.wirewave.wirewave.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Integer> {

    List<ProductAttribute> findByProductId(Integer productId);

    List<ProductAttribute> findByAttributeIdAndValue(Integer attributeId, String value);
}
