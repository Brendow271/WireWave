package com.wirewave.wirewave.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories_subcategories")
public class CategoriesSubcategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_categories", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_subcategories", nullable = false)
    private Category subcategory;

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

    public Category getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Category subcategory) {
        this.subcategory = subcategory;
    }
}
