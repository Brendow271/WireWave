package com.wirewave.wirewave.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_position")
public class OrderPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Связь с продуктом
    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;

    // Количество товара
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // Связь с корзиной
    @ManyToOne
    @JoinColumn(name = "id_basket", nullable = false)
    private Basket basket;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }
}
