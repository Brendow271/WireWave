package com.wirewave.wirewave.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "basket")
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Пользователь, связанный с корзиной
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    // Список позиций заказа, связанных с корзиной
    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderPosition> orderPositions = new ArrayList<>();

    // Общая стоимость корзины (вычисляется на основе позиций)
    @Transient
    public BigDecimal getTotalPrice() {
        return orderPositions.stream()
                .map(op -> {
                    BigDecimal productPrice = op.getProduct().getDiscountPrice() != null
                            ? op.getProduct().getDiscountPrice()
                            : op.getProduct().getPrice();
                    return productPrice.multiply(BigDecimal.valueOf(op.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderPosition> getOrderPositions() {
        return orderPositions;
    }

    public void setOrderPositions(List<OrderPosition> orderPositions) {
        this.orderPositions = orderPositions;
    }
}
