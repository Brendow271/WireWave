package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.entity.Warehouse;
import com.wirewave.wirewave.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    // Создание нового склада
    @PostMapping
    public ResponseEntity<Warehouse> createWarehouse(@Valid @RequestBody Warehouse warehouse) {
        List<Warehouse> existingWarehouses = warehouseService.getAllWarehouses();
        if (!existingWarehouses.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Warehouse savedWarehouse = warehouseService.saveWarehouse(warehouse);
        return ResponseEntity.ok(savedWarehouse);
    }

    // Получение всех складов
    @GetMapping
    public ResponseEntity<List<Warehouse>> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    // Получение склада по ID
    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Integer id) {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        return warehouse != null ? ResponseEntity.ok(warehouse) : ResponseEntity.notFound().build();
    }

    // Обновление склада
    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable Integer id, @Valid @RequestBody Warehouse warehouseDetails) {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        if (warehouse == null) {
            return ResponseEntity.notFound().build();
        }

        warehouse.setProduct(warehouseDetails.getProduct());
        warehouse.setQuantity(warehouseDetails.getQuantity());

        Warehouse updatedWarehouse = warehouseService.saveWarehouse(warehouse);
        return ResponseEntity.ok(updatedWarehouse);
    }

    // Удаление склада по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Integer id) {
        if (warehouseService.getWarehouseById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}
