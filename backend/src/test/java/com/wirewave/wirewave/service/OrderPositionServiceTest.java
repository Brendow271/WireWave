package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.OrderPosition;
import com.wirewave.wirewave.repository.OrderPositionRepository;
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
class OrderPositionServiceTest {

    @Mock
    private OrderPositionRepository orderPositionRepository;

    @InjectMocks
    private OrderPositionService orderPositionService;

    private OrderPosition orderPosition;

    @BeforeEach
    void setUp() {
        orderPosition = new OrderPosition();
        orderPosition.setId(1);
    }

    // Тест на получение всех позиций в заказах
    @Test
    void shouldReturnAllOrderPositions() {
        when(orderPositionRepository.findAll()).thenReturn(List.of(orderPosition));

        List<OrderPosition> result = orderPositionService.getAllOrderPositions();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(orderPosition);
    }

    // Тест на получение позиции в заказе по ID
    @Test
    void shouldReturnOrderPositionById() {
        when(orderPositionRepository.findById(1)).thenReturn(Optional.of(orderPosition));

        OrderPosition result = orderPositionService.getOrderPositionById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderPosition.getId());
    }

    // Тест на получение позиции в заказе по ID, если она не найдена
    @Test
    void shouldReturnNullIfOrderPositionNotExists() {
        when(orderPositionRepository.findById(1)).thenReturn(Optional.empty());

        OrderPosition result = orderPositionService.getOrderPositionById(1);

        assertThat(result).isNull();
    }

    // Тест на сохранение позиции в заказе
    @Test
    void shouldSaveOrderPosition() {
        when(orderPositionRepository.save(any(OrderPosition.class))).thenReturn(orderPosition);

        OrderPosition result = orderPositionService.saveOrderPosition(orderPosition);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderPosition.getId());
    }

    // Тест на удаление позиции в заказе
    @Test
    void shouldDeleteOrderPosition() {
        doNothing().when(orderPositionRepository).deleteById(1);

        orderPositionService.deleteOrderPosition(1);

        verify(orderPositionRepository, times(1)).deleteById(1);
    }
}
