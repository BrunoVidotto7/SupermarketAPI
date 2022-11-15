package com.qikserve.supermarket.handler.impl;

import static com.qikserve.supermarket.util.ProductInfo.BURGER_ID;
import static com.qikserve.supermarket.util.ProductInfo.BURGER_NAME;
import static com.qikserve.supermarket.util.ProductInfo.BURGER_PRICE;
import static com.qikserve.supermarket.util.ProductInfo.BUY_X_GET_Y_FREE;
import static com.qikserve.supermarket.util.ProductInfo.BUY_X_GET_Y_FREE_ID;
import static com.qikserve.supermarket.util.ProductInfo.BUY_X_GET_Y_FREE_QTY;
import static com.qikserve.supermarket.util.ProductInfo.BUY_X_GET_Y_FREE_REQUIRED_QTY;
import static com.qikserve.supermarket.util.ProductInfo.FLAT_PERCENT;
import static com.qikserve.supermarket.util.ProductInfo.FLAT_PERCENT_AMOUNT;
import static com.qikserve.supermarket.util.ProductInfo.FLAT_PERCENT_ID;
import static com.qikserve.supermarket.util.ProductInfo.PIZZA_ID;
import static com.qikserve.supermarket.util.ProductInfo.PIZZA_NAME;
import static com.qikserve.supermarket.util.ProductInfo.PIZZA_PRICE;
import static com.qikserve.supermarket.util.ProductInfo.QTY_BASED_PRICE_OVERRIDE;
import static com.qikserve.supermarket.util.ProductInfo.QTY_BASED_PRICE_OVERRIDE_ID;
import static com.qikserve.supermarket.util.ProductInfo.QTY_BASED_PRICE_OVERRIDE_PRICE;
import static com.qikserve.supermarket.util.ProductInfo.QTY_BASED_PRICE_OVERRIDE_REQUIRED_QTY;
import static com.qikserve.supermarket.util.ProductInfo.SALAD_ID;
import static com.qikserve.supermarket.util.ProductInfo.SALAD_NAME;
import static com.qikserve.supermarket.util.ProductInfo.SALAD_PRICE;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.qikserve.supermarket.model.Basket;
import com.qikserve.supermarket.model.BasketProduct;
import com.qikserve.supermarket.model.BasketProductPK;
import com.qikserve.supermarket.model.ExpectedTotals;
import com.qikserve.supermarket.pojo.Product;
import com.qikserve.supermarket.pojo.Promotion;
import com.qikserve.supermarket.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculateTotalsHandlerImplTest {

    private static final double TOTAL_RAW = 76.91;
    private static final double TOTAL_PROMO = 25.47;
    private static final double TOTAL_PAYABLE = 51.44;
    CalculateTotalsHandlerImpl calculateTotalsHandler;
    @Mock
    ProductService productService;

    @BeforeEach
    void init() {
        calculateTotalsHandler = new CalculateTotalsHandlerImpl(productService);
    }

    @Test
    void whenCalculateExpectedTotals_thenGetSuccess() {
        Product burger = buildBurguer();

        Product pizza = buildPizza();

        Product salad = buildSalad();

        Basket basket = Basket.builder()
            .id(1)
            .status("OPEN")
            .build();

        int burgerQuantity = 4;
        BasketProduct burgerOrder = buildBurgerOrder(basket, burgerQuantity);

        int pizzaQuantity = 2;
        BasketProduct pizzaOrder = buildPizzaOrder(basket, pizzaQuantity);

        int saladQuantity = 3;
        BasketProduct saladOrder = buildSaladOrder(basket, saladQuantity);

        List<BasketProduct> basketProducts = Arrays.asList(burgerOrder, pizzaOrder, saladOrder);

        basket.setBasketProducts(basketProducts);

        Mockito.when(productService.getProductById(BURGER_ID.getName())).thenReturn(burger);
        Mockito.when(productService.getProductById(PIZZA_ID.getName())).thenReturn(pizza);
        Mockito.when(productService.getProductById(SALAD_ID.getName())).thenReturn(salad);

        ExpectedTotals expectedTotals = calculateTotalsHandler.calculateExpectedTotals(basket);

        assertEquals(TOTAL_RAW, expectedTotals.getRawTotal());
        assertEquals(TOTAL_PROMO, expectedTotals.getTotalPromos());
        assertEquals(TOTAL_PAYABLE, expectedTotals.getTotalPayable());
    }

    private BasketProduct buildSaladOrder(Basket basket, int quantity) {
        return BasketProduct.builder()
            .quantity(quantity)
            .productName(SALAD_NAME.getName())
            .pk(BasketProductPK.builder().productId(SALAD_ID.getName()).basket(basket).build())
            .build();
    }

    private BasketProduct buildPizzaOrder(Basket basket, int quantity) {
        return BasketProduct.builder()
            .quantity(quantity)
            .productName(PIZZA_NAME.getName())
            .pk(BasketProductPK.builder().productId(PIZZA_ID.getName()).basket(basket).build())
            .build();
    }

    private BasketProduct buildBurgerOrder(Basket basket, int quantity) {
        return BasketProduct.builder()
            .quantity(quantity)
            .productName(BURGER_NAME.getName())
            .pk(BasketProductPK.builder().productId(BURGER_ID.getName()).basket(basket).build())
            .build();
    }

    private Product buildSalad() {
        return Product.builder()
            .id(SALAD_ID.getName())
            .name(SALAD_NAME.getName())
            .price(parseDouble(SALAD_PRICE.getName()))
            .promotions(Collections.singletonList(Promotion.builder()
                .id(FLAT_PERCENT_ID.getName())
                .type(FLAT_PERCENT.getName())
                .amount(parseInt(FLAT_PERCENT_AMOUNT.getName()))
                .build()))
            .build();
    }

    private Product buildPizza() {
        return Product.builder()
            .id(PIZZA_ID.getName())
            .name(PIZZA_NAME.getName())
            .price(parseDouble(PIZZA_PRICE.getName()))
            .promotions(Collections.singletonList(Promotion.builder()
                .id(QTY_BASED_PRICE_OVERRIDE_ID.getName())
                .type(QTY_BASED_PRICE_OVERRIDE.getName())
                .requiredQuantity(parseInt(QTY_BASED_PRICE_OVERRIDE_REQUIRED_QTY.getName()))
                .price(parseDouble(QTY_BASED_PRICE_OVERRIDE_PRICE.getName()))
                .build()))
            .build();
    }

    private Product buildBurguer() {
        return Product.builder()
            .id(BURGER_ID.getName())
            .name(BURGER_NAME.getName())
            .price(parseDouble(BURGER_PRICE.getName()))
            .promotions(Collections.singletonList(Promotion.builder()
                .id(BUY_X_GET_Y_FREE_ID.getName())
                .type(BUY_X_GET_Y_FREE.getName())
                .requiredQuantity(parseInt(BUY_X_GET_Y_FREE_REQUIRED_QTY.getName()))
                .freeQuantity(parseInt(BUY_X_GET_Y_FREE_QTY.getName()))
                .build()))
            .build();
    }
}