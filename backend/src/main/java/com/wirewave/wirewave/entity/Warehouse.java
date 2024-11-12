package com.wirewave.wirewave.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

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

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Warehouse)) return false;
        Warehouse warehouse = (Warehouse) o;
        return id != null && id.equals(warehouse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
