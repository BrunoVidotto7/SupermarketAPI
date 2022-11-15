package com.qikserve.supermarket.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.qikserve.supermarket.model.BasketProduct;

public interface BasketProductService {
    BasketProduct create(@NotNull(message = "The products for order cannot be null.") @Valid BasketProduct basketProduct);
}
