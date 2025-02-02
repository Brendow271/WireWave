package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Category;
import com.wirewave.wirewave.entity.Comment;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.repository.ProductCategoriesRepository;
import com.wirewave.wirewave.repository.ProductRepository;
import com.wirewave.wirewave.repository.CommentRepository;
import com.wirewave.wirewave.entity.ProductCategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ProductCategoriesRepository productCategoriesRepository;

    // Получить все продукты
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Получить продукт по ID
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    // Сохранить продукт
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Удалить продукт по ID
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    // Пересчитать и обновить среднюю оценку продукта
    public void updateAverageRating(Integer productId) {
        List<Comment> comments = commentService.getCommentsByProductId(productId);
        double averageRating = comments.stream()
                .mapToInt(Comment::getEstimation)
                .average()
                .orElse(0.0);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Продукт с ID " + productId + " не найден"));
        product.setAverageRating(averageRating);
        productRepository.save(product);
    }

    public List<Category> getCategoriesByProduct(Integer productId) {
        return productCategoriesRepository.findByProductId(productId)
                .stream()
                .map(ProductCategories::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }
}
