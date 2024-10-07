package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.*;
import com.wirewave.wirewave.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesSubcategoriesService {

    @Autowired
    private CategoriesSubcategoriesRepository categoriesSubcategoriesRepository;

    public List<CategoriesSubcategories> getAllCategoriesSubcategories() {
        return categoriesSubcategoriesRepository.findAll();
    }

    public CategoriesSubcategories getCategoriesSubcategoriesById(Integer id) {
        return categoriesSubcategoriesRepository.findById(id).orElse(null);
    }

    public CategoriesSubcategories saveCategoriesSubcategories(CategoriesSubcategories categoriesSubcategories) {
        return categoriesSubcategoriesRepository.save(categoriesSubcategories);
    }

    public void deleteCategoriesSubcategories(Integer id) {
        categoriesSubcategoriesRepository.deleteById(id);
    }
}
