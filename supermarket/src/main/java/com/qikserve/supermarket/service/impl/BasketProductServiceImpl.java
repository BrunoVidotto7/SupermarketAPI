package com.qikserve.supermarket.service.impl;

import java.util.List;

import com.qikserve.supermarket.model.Basket;
import com.qikserve.supermarket.model.BasketProduct;
import com.qikserve.supermarket.repository.BasketProductRepository;
import com.qikserve.supermarket.service.BasketProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class BasketProductServiceImpl implements BasketProductService {

    private final BasketProductRepository basketProductRepository;

    @Override
    public BasketProduct create(BasketProduct basketProduct) {
        return basketProductRepository.save(basketProduct);
    }

    @Override
    public List<BasketProduct> saveAll(List<BasketProduct> basketProducts) {
        return basketProductRepository.saveAll(basketProducts);
    }
}
