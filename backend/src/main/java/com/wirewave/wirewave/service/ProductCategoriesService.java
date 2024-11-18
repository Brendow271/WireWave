package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.ProductCategories;
import com.wirewave.wirewave.repository.ProductCategoriesRepository;
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

    public ProductCategories getProductCategoryById(Integer id) {
        return productCategoriesRepository.findById(id).orElse(null);
    }

    public ProductCategories saveProductCategory(ProductCategories productCategories) {
        return productCategoriesRepository.save(productCategories);
    }

    public void deleteProductCategory(Integer id) {
        productCategoriesRepository.deleteById(id);
    }
}
