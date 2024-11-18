package com.wirewave.wirewave.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

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

    // Позиция заказа, связанная с корзиной
    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private OrderPosition orderPosition;

    // Общая стоимость корзины
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

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

    public OrderPosition getOrderPosition() {
        return orderPosition;
    }

    public void setOrderPosition(OrderPosition orderPosition) {
        this.orderPosition = orderPosition;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
