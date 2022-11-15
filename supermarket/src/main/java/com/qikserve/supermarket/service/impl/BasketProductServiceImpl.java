package com.qikserve.supermarket.service.impl;

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
}
