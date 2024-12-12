package com.wirewave.wirewave.repository;

import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    List<Warehouse> findByProduct(Product product);

}
