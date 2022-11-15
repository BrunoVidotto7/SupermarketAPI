package com.qikserve.supermarket.service;

import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.qikserve.supermarket.model.Basket;

public interface BasketService {
    @NotNull Iterable<Basket> getAllBaskets();

    Basket loadBaskedById(Integer id);

    Basket create(@NotNull(message = "The order cannot be null.") @Valid Basket basket);

    void update(@NotNull(message = "The order cannot be null.") @Valid Basket basket);
}
