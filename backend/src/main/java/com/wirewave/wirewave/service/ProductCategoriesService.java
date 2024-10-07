package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.*;
import com.wirewave.wirewave.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoriesService {

    @Autowired
    private ProductCategoriesRepository productCategoriesRepository;

    public List<ProductCategories> getAllProductCategories() {
        return productCategoriesRepository.findAll();
    }

    public ProductCategories getProductCategoriesById(Integer id) {
        return productCategoriesRepository.findById(id).orElse(null);
    }

    public ProductCategories saveProductCategories(ProductCategories productCategories) {
        return productCategoriesRepository.save(productCategories);
    }

    public void deleteProductCategories(Integer id) {
        productCategoriesRepository.deleteById(id);
    }
}