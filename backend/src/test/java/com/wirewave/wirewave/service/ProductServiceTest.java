package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Category;
import com.wirewave.wirewave.entity.Comment;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.ProductCategories;
import com.wirewave.wirewave.repository.ProductCategoriesRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CommentService commentService;

    @Mock
    private ProductCategoriesRepository productCategoriesRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Comment comment;
    private Category category;
    private ProductCategories productCategories;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setProductName("Test Product");

        comment = new Comment();
        comment.setEstimation(5);
        comment.setProduct(product);

        category = new Category();
        category.setId(1);
        category.setCategoryName("Electronics");

        productCategories = new ProductCategories();
        productCategories.setProduct(product);
        productCategories.setCategory(category);
    }

    // Тест на получение всех продуктов
    @Test
    void shouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> products = productService.getAllProducts();

        assertThat(products).hasSize(1);
        assertThat(products.get(0)).isEqualTo(product);
    }

    // Тест на получение продукта по ID
    @Test
    void shouldReturnProductById() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1);

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getId()).isEqualTo(product.getId());
    }

    // Тест на получение продукта по ID, если его нет
    @Test
    void shouldReturnNullIfProductNotExists() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Product foundProduct = productService.getProductById(1);

        assertThat(foundProduct).isNull();
    }

    // Тест на сохранение продукта
    @Test
    void shouldSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.saveProduct(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(product.getId());
    }

    // Тест на удаление продукта
    @Test
    void shouldDeleteProduct() {
        doNothing().when(productRepository).deleteById(1);

        productService.deleteProduct(1);

        verify(productRepository, times(1)).deleteById(1);
    }

    // Тест на обновление средней оценки продукта
    @Test
    void shouldUpdateAverageRating() {
        when(commentService.getCommentsByProductId(1)).thenReturn(List.of(comment));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.updateAverageRating(1);

        assertThat(product.getAverageRating()).isEqualTo(5.0);
    }

    // Тест на обновление средней оценки, если нет комментариев
    @Test
    void shouldSetZeroRatingIfNoComments() {
        when(commentService.getCommentsByProductId(1)).thenReturn(List.of());
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.updateAverageRating(1);

        assertThat(product.getAverageRating()).isEqualTo(0.0);
    }

    // Тест на получение категорий продукта
    @Test
    void shouldReturnCategoriesByProduct() {
        when(productCategoriesRepository.findByProductId(1)).thenReturn(List.of(productCategories));

        List<Category> categories = productService.getCategoriesByProduct(1);

        assertThat(categories).hasSize(1);
        assertThat(categories.get(0)).isEqualTo(category);
    }
}
