package com.qikserve.supermarket.service.impl;

import java.util.Optional;
import javax.transaction.Transactional;

import com.qikserve.supermarket.error.exception.ResourceNotFoundException;
import com.qikserve.supermarket.model.Basket;
import com.qikserve.supermarket.repository.BasketRepository;
import com.qikserve.supermarket.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
@Transactional
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;

    @Override
    public Iterable<Basket> getAllBaskets() {
        return basketRepository.findAll();
    }

    public Basket loadBaskedById(Integer id){
        final Optional<Basket> basketOptional = basketRepository.findById(id);
        if (basketOptional.isPresent()) {
           return basketOptional.get();
        }
        throw new ResourceNotFoundException("Basket not found");
    }

    @Override
    public Basket create(Basket basket) {
        return basketRepository.save(basket);
    }

    @Override
    public void update(Basket basket) {
        basketRepository.save(basket);
    }
}
