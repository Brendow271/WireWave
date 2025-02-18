package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.Warehouse;
import com.wirewave.wirewave.repository.ProductRepository;
import com.wirewave.wirewave.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    private Warehouse warehouse;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);

        warehouse = new Warehouse();
        warehouse.setId(1);
        warehouse.setProduct(product);
        warehouse.setQuantity(10);
    }

    // Тест на получение всех складов
    @Test
    void shouldReturnAllWarehouses() {
        when(warehouseRepository.findAll()).thenReturn(List.of(warehouse));

        List<Warehouse> warehouses = warehouseService.getAllWarehouses();

        assertThat(warehouses).hasSize(1);
        assertThat(warehouses.get(0)).isEqualTo(warehouse);
    }

    // Тест на получение склада по ID
    @Test
    void shouldReturnWarehouseById() {
        when(warehouseRepository.findById(1)).thenReturn(Optional.of(warehouse));

        Warehouse foundWarehouse = warehouseService.getWarehouseById(1);

        assertThat(foundWarehouse).isNotNull();
        assertThat(foundWarehouse.getId()).isEqualTo(warehouse.getId());
    }

    // Тест на получение склада по ID, если его нет
    @Test
    void shouldReturnNullIfWarehouseNotFound() {
        when(warehouseRepository.findById(1)).thenReturn(Optional.empty());

        Warehouse foundWarehouse = warehouseService.getWarehouseById(1);

        assertThat(foundWarehouse).isNull();
    }

    // Тест на сохранение склада
    @Test
    void shouldSaveWarehouse() {
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        Warehouse savedWarehouse = warehouseService.saveWarehouse(warehouse);

        assertThat(savedWarehouse).isNotNull();
        assertThat(savedWarehouse.getId()).isEqualTo(warehouse.getId());
    }

    // Тест на удаление склада
    @Test
    void shouldDeleteWarehouse() {
        doNothing().when(warehouseRepository).deleteById(1);

        warehouseService.deleteWarehouse(1);

        verify(warehouseRepository, times(1)).deleteById(1);
    }

    // Тест на получение общего количества товара на складе
    @Test
    void shouldReturnTotalStockForProduct() {
        when(warehouseRepository.findByProduct(product)).thenReturn(warehouse);

        int totalStock = warehouseService.getTotalStockForProduct(product);

        assertThat(totalStock).isEqualTo(warehouse.getQuantity());
    }

    // Тест на получение количества товара, если склад пустой
    @Test
    void shouldReturnZeroIfProductNotInWarehouse() {
        when(warehouseRepository.findByProduct(product)).thenReturn(null);

        int totalStock = warehouseService.getTotalStockForProduct(product);

        assertThat(totalStock).isZero();
    }

    // Тест на статус доступности товара: "В наличии"
    @Test
    void shouldReturnAvailableIfStockIsAboveFive() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(warehouseRepository.findByProduct(product)).thenReturn(warehouse);

        String availabilityStatus = warehouseService.getProductAvailabilityStatus(1);

        assertThat(availabilityStatus).isEqualTo("В наличии");
    }

    // Тест на статус доступности товара: "Мало"
    @Test
    void shouldReturnLowStockIfStockIsFiveOrLess() {
        warehouse.setQuantity(5);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(warehouseRepository.findByProduct(product)).thenReturn(warehouse);

        String availabilityStatus = warehouseService.getProductAvailabilityStatus(1);

        assertThat(availabilityStatus).isEqualTo("Мало");
    }

    // Тест на статус доступности товара: "Нет в наличии"
    @Test
    void shouldReturnOutOfStockIfStockIsZero() {
        warehouse.setQuantity(0);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(warehouseRepository.findByProduct(product)).thenReturn(warehouse);

        String availabilityStatus = warehouseService.getProductAvailabilityStatus(1);

        assertThat(availabilityStatus).isEqualTo("Нет в наличии");
    }

    // Тест на статус доступности товара, если продукт не найден
    @Test
    void shouldReturnNullIfProductDoesNotExist() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        String availabilityStatus = warehouseService.getProductAvailabilityStatus(1);

        assertThat(availabilityStatus).isNull();
    }
}
