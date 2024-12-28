package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.Warehouse;
import com.wirewave.wirewave.repository.ProductRepository;
import com.wirewave.wirewave.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Warehouse getWarehouseById(Integer id) {
        return warehouseRepository.findById(id).orElse(null);
    }

    public Warehouse saveWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public void deleteWarehouse(Integer id) {
        warehouseRepository.deleteById(id);
    }

    public int getTotalStockForProduct(Product product) {
        Warehouse warehouse = warehouseRepository.findByProduct(product);
        return warehouse != null ? warehouse.getQuantity() : 0;
    }

    public String getProductAvailabilityStatus(Integer productId) {
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return null;
        }

        Warehouse warehouse = warehouseRepository.findByProduct(product);
        if (warehouse == null || warehouse.getQuantity() == 0) {
            return "Нет в наличии";
        } else if (warehouse.getQuantity() <= 5) {
            return "Мало";
        } else {
            return "В наличии";
        }
    }

}
