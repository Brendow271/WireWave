package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.*;
import com.wirewave.wirewave.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderPositionService {

    @Autowired
    private OrderPositionRepository orderPositionRepository;

    public List<OrderPosition> getAllOrderPositions() {
        return orderPositionRepository.findAll();
    }

    public OrderPosition getOrderPositionById(Integer id) {
        return orderPositionRepository.findById(id).orElse(null);
    }

    public OrderPosition saveOrderPosition(OrderPosition orderPosition) {
        return orderPositionRepository.save(orderPosition);
    }

    public void deleteOrderPosition(Integer id) {
        orderPositionRepository.deleteById(id);
    }
}
