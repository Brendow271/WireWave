package com.wirewave.wirewave.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_categories")
public class ProductCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_categories", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;

    // Конструкторы, если необходимо
    public ProductCategories() {
    }

    public ProductCategories(Category category, Product product) {
        this.category = category;
        this.product = product;
    }

    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Переопределение методов equals и hashCode для корректной работы с коллекциями
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductCategories)) return false;
        ProductCategories that = (ProductCategories) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProductCategories{" +
                "id=" + id +
                ", category=" + category +
                ", product=" + product +
                '}';
    }
}
