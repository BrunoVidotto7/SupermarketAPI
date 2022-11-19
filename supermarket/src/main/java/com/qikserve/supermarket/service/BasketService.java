package com.qikserve.supermarket.service;

import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.qikserve.supermarket.dto.BasketForm;
import com.qikserve.supermarket.model.Basket;
import org.springframework.web.bind.annotation.PathVariable;

public interface BasketService {
    @NotNull Iterable<Basket> getAllBaskets();

    Basket loadBaskedById(Integer id);

    Basket create(@NotNull(message = "The order cannot be null.") @Valid Basket basket);

    Basket create(@NotNull(message = "The form cannot be null.") BasketForm form);

    void update(@NotNull(message = "The order cannot be null.") @Valid Basket basket);

    Basket addItems(@NotNull(message = "The form cannot be null.") BasketForm form, @NotNull(message = "The id cannot be null.") Integer id);

    Basket checkout(@NotNull(message = "The id cannot be null.") Integer id);

    Basket pay(@NotNull(message = "The id cannot be null.") Integer id);
}
