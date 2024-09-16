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

}