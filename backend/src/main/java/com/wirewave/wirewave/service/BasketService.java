package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Basket;
import com.wirewave.wirewave.repository.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;

    public List<Basket> getAllBaskets() {
        return basketRepository.findAll();
    }

    public Basket getBasketById(Integer id) {
        return basketRepository.findById(id).orElse(null);
    }

    public Basket saveBasket(Basket basket) {
        return basketRepository.save(basket);
    }

    public void deleteBasket(Integer id) {
        basketRepository.deleteById(id);
    }
}
