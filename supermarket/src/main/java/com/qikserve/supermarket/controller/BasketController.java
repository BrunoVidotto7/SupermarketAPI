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

    private final ProductService productService;
    private final BasketService basketService;
    private final BasketProductService basketProductService;
    private final ExpectedTotalsService expectedTotalsService;


    @GetMapping( {"", "/"})
    public @NotNull Iterable<Basket> listBaskets() {
        return basketService.getAllBaskets();
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<Basket> create(@RequestBody BasketForm form) {
        validateForm(form);
        final var formDtos = form.getProductBaskets();

        Basket basket = new Basket();
        basket.setStatus(BasketStatus.OPEN.name());
        basketService.create(basket);

        final List<BasketProduct> basketProducts = loadBasketProducts(formDtos, basket);
        basket.setBasketProducts(basketProducts);

        ExpectedTotals expectedTotals = loadExpectedTotals(basket);
        basket.setExpectedTotals(expectedTotals);

        basketService.update(basket);

        HttpHeaders headers = loadUri(basket);

        return new ResponseEntity<>(basket, headers, HttpStatus.CREATED);
    }

    @PutMapping("/add/{id}")
    @Transactional
    public ResponseEntity<Basket> addItems(@RequestBody BasketForm form, @PathVariable("id") Integer id) {
        validateForm(form);
        final var formDtos = form.getProductBaskets();
        final var basket = basketService.loadBaskedById(id);

        handleBasketStatus(basket);

        List<BasketProduct> basketProducts = basket.getBasketProducts();
        basketProducts.addAll(loadBasketProducts(formDtos, basket));

        ExpectedTotals expectedTotals = loadExpectedTotals(basket);
        basket.setExpectedTotals(expectedTotals);

        basketService.update(basket);

        HttpHeaders headers = loadUri(basket);

        return new ResponseEntity<>(basket, headers, HttpStatus.OK);
    }

    private void handleBasketStatus(Basket basket) {
        if(BasketStatus.PAID.name().equals(basket.getStatus())) {
            throw new BasketAlreadyClosedException();
        }
        if(BasketStatus.CHECKOUT.name().equals(basket.getStatus())) {
            basket.setStatus(BasketStatus.OPEN.name());
        }
    }

    @PutMapping("/checkout/{id}")
    public ResponseEntity<Basket> checkout(@PathVariable("id") Integer id) {
        Basket basket = basketService.loadBaskedById(id);
        basket.setStatus(BasketStatus.CHECKOUT.name());

        basketService.update(basket);

        final HttpHeaders headers = loadUri(basket);

        return new ResponseEntity<>(basket, headers, HttpStatus.OK);
    }

    @PutMapping("/pay/{id}")
    public ResponseEntity<Basket> pay(@PathVariable("id") Integer id) {
        Basket basket = basketService.loadBaskedById(id);
        basket.setStatus(BasketStatus.PAID.name());

        basketService.update(basket);

        final HttpHeaders headers = loadUri(basket);

        return new ResponseEntity<>(basket, headers, HttpStatus.OK);
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

    private List<BasketProduct> loadBasketProducts(List<BasketProductDto> formDtos, Basket basket) {
        final List<BasketProduct> basketProducts = new ArrayList<>();
        for (BasketProductDto dto : formDtos) {
            BasketProduct basketProduct = BasketProduct.builder()
                .pk(new BasketProductPK(basket, dto.getProductId()))
                .productName(loadProductName(dto).getName())
                .quantity(dto.getQuantity())
                .build();

            basketProductService.create(basketProduct);
            basketProducts.add(basketProduct);
        }
        return basketProducts;
    }

    private ProductName loadProductName(BasketProductDto dto) {
        return ProductName.fromString(productService.getProductById(dto.getProductId()).getName());
    }

    private ExpectedTotals loadExpectedTotals(Basket basket) {
        final ExpectedTotals expectedTotals = expectedTotalsService.calculateExpectedTotals(basket);
        expectedTotalsService.create(expectedTotals);
        return expectedTotals;
    }

    private void validateForm(BasketForm form) {
        if (form.getProductBaskets() == null) {
            throw new InvalidFormException("Invalid form. Please check it and try again.");
        } else {
            validateProductBaskets(form);
        }
    }

    private void validateProductBaskets(BasketForm form) {
        final List<BasketProductDto> list = form.getProductBaskets()
            .stream()
            .filter(basketProductDto -> Objects.isNull(productService.getProductById(
                basketProductDto.getProductId()
            ))).toList();

        if (!CollectionUtils.isEmpty(list)) {
            throw new ResourceNotFoundException("Product not found");
        }
    }

}
