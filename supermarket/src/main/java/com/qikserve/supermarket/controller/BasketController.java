package com.qikserve.supermarket.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import com.qikserve.supermarket.dto.BasketForm;
import com.qikserve.supermarket.dto.BasketProductDto;
import com.qikserve.supermarket.error.exception.BasketAlreadyClosedException;
import com.qikserve.supermarket.error.exception.InvalidFormException;
import com.qikserve.supermarket.error.exception.ResourceNotFoundException;
import com.qikserve.supermarket.model.Basket;
import com.qikserve.supermarket.model.BasketProduct;
import com.qikserve.supermarket.model.BasketProductPK;
import com.qikserve.supermarket.model.BasketStatus;
import com.qikserve.supermarket.model.ExpectedTotals;
import com.qikserve.supermarket.model.ProductName;
import com.qikserve.supermarket.service.BasketProductService;
import com.qikserve.supermarket.service.BasketService;
import com.qikserve.supermarket.service.ExpectedTotalsService;
import com.qikserve.supermarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/baskets")
public class BasketController {
    private final BasketService basketService;


    @GetMapping( {"", "/"})
    public @NotNull Iterable<Basket> listBaskets() {
        return basketService.getAllBaskets();
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<Basket> create(@RequestBody BasketForm form) {
        final Basket basket = basketService.create(form);
        HttpHeaders headers = loadUri(basket);
        return new ResponseEntity<>(basket, headers, HttpStatus.CREATED);
    }

    @PatchMapping ("/add/{id}")
    @Transactional
    public ResponseEntity<Basket> addItems(@RequestBody BasketForm form, @PathVariable("id") Integer id) {
        final Basket updatedBasket = basketService.addItems(form, id);
        HttpHeaders headers = loadUri(updatedBasket);
        return new ResponseEntity<>(updatedBasket, headers, HttpStatus.OK);
    }


    @PatchMapping("/checkout/{id}")
    public ResponseEntity<Basket> checkout(@PathVariable("id") Integer id) {
        final Basket updatedBasket = basketService.checkout(id);
        final HttpHeaders headers = loadUri(updatedBasket);
        return new ResponseEntity<>(updatedBasket, headers, HttpStatus.OK);
    }

    @PatchMapping("/pay/{id}")
    public ResponseEntity<Basket> pay(@PathVariable("id") Integer id) {
        final Basket updatedBasket = basketService.pay(id);
        final HttpHeaders headers = loadUri(updatedBasket);
        return new ResponseEntity<>(updatedBasket, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Basket> loadBaskedById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(basketService.loadBaskedById(id), HttpStatus.OK);
    }

    private HttpHeaders loadUri(Basket basket) {
        final String uri = ServletUriComponentsBuilder
            .fromCurrentServletMapping()
            .path("/baskets/{id}")
            .buildAndExpand(basket.getId())
            .toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);
        return headers;
    }

}
