package com.qikserve.supermarket.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import com.qikserve.supermarket.repository.BasketRepository;
import com.qikserve.supermarket.service.BasketProductService;
import com.qikserve.supermarket.service.BasketService;
import com.qikserve.supermarket.service.ExpectedTotalsService;
import com.qikserve.supermarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor

@Service
@Transactional
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;
    private final BasketProductService basketProductService;
    private final ProductService productService;

    private final ExpectedTotalsService expectedTotalsService;

    @Override
    public Iterable<Basket> getAllBaskets() {
        return basketRepository.findAll();
    }

    public Basket loadBaskedById(Integer id) {
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
    public Basket create(BasketForm form) {
        validateForm(form);
        final List<BasketProductDto> formDtos = form.getProductBaskets();

        Basket basket = new Basket();
        basket.setStatus(BasketStatus.OPEN.name());
        this.create(basket);

        final List<BasketProduct> basketProducts = loadBasketProducts(formDtos, basket);
        basketProductService.saveAll(basketProducts);
        basket.setBasketProducts(basketProducts);

        ExpectedTotals expectedTotals = loadExpectedTotals(basket);
        basket.setExpectedTotals(expectedTotals);

        this.update(basket);

        return basket;
    }

    @Override
    public void update(Basket basket) {
        basketRepository.save(basket);
    }

    @Override
    public Basket addItems(BasketForm form, Integer id) {
        validateForm(form);
        final List<BasketProductDto> formDtos = form.getProductBaskets();
        final Basket basket = this.loadBaskedById(id);

        handleBasketStatus(basket);

        List<BasketProduct> formBasketProducts = loadBasketProducts(formDtos, basket);
        List<BasketProduct> newBasketProducts = loadNewToBasketItems(basket, formBasketProducts);
        basketProductService.saveAll(newBasketProducts);
        basket.setBasketProducts(newBasketProducts);

        ExpectedTotals expectedTotals = loadNewExpectedTotals(basket);
        basket.setExpectedTotals(expectedTotals);

        this.update(basket);

        return basket;
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

    @Override
    public Basket checkout(@NotNull(message = "The id cannot be null.") Integer id){
        final Basket basket = this.loadBaskedById(id);
        basket.setStatus(BasketStatus.CHECKOUT.name());
        this.update(basket);

        return basket;
    }

    @Override
    public Basket pay(@NotNull(message = "The id cannot be null.") Integer id){
        Basket basket = this.loadBaskedById(id);
        basket.setStatus(BasketStatus.PAID.name());

        this.update(basket);

        return basket;
    }

    private List<BasketProduct> loadBasketProducts(List<BasketProductDto> formDtos, Basket basket) {
        final List<BasketProduct> basketProducts = new ArrayList<>();
        for (BasketProductDto dto : formDtos) {
            BasketProduct basketProduct = BasketProduct.builder()
                .pk(new BasketProductPK(basket, dto.getProductId()))
                .productName(loadProductName(dto).getName())
                .quantity(dto.getQuantity())
                .build();
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

    private ExpectedTotals loadNewExpectedTotals(Basket basket) {
        final ExpectedTotals newExpectedTotals = expectedTotalsService.calculateExpectedTotals(basket);
        ExpectedTotals expectedTotals = basket.getExpectedTotals();
        newExpectedTotals.setId(expectedTotals.getId());
        expectedTotalsService.create(newExpectedTotals);
        return expectedTotals;
    }

    private void handleBasketStatus(Basket basket) {
        if (BasketStatus.PAID.name().equals(basket.getStatus())) {
            throw new BasketAlreadyClosedException();
        }
        if (BasketStatus.CHECKOUT.name().equals(basket.getStatus())) {
            basket.setStatus(BasketStatus.OPEN.name());
        }
    }

    private List<BasketProduct> loadNewToBasketItems(Basket basket, List<BasketProduct> formBasketProducts) {
        List<BasketProduct> basketProducts = basket.getBasketProducts();
        return addToBasket(formBasketProducts, basketProducts);
    }

    private List<BasketProduct> addToBasket(List<BasketProduct> formBasketProducts, List<BasketProduct> basketProducts) {
        if (basketProducts != null) {
            for (BasketProduct formProduct : formBasketProducts) {
                if(basketProducts.contains(formProduct)) {
                    basketProducts.forEach(product -> updateQuantityIfSameProducts(product, formProduct));
                } else {
                    basketProducts.add(formProduct);
                }
            }
            return basketProducts;
        } else {
            return new ArrayList<>(formBasketProducts);
        }
    }

    private void updateQuantityIfSameProducts(BasketProduct product, BasketProduct formProduct) {
        if (product.equals(formProduct)) {
            product.setQuantity(product.getQuantity() + formProduct.getQuantity());
        }
    }

}
