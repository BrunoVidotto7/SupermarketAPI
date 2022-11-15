package com.qikserve.supermarket.handler.impl;

import static com.qikserve.supermarket.util.SupermarketUtil.round;
import static com.qikserve.supermarket.util.SupermarketUtil.zipToMap;

import java.util.List;
import java.util.Map;

import com.qikserve.supermarket.handler.CalculateTotalsHandler;
import com.qikserve.supermarket.model.Basket;
import com.qikserve.supermarket.model.BasketProduct;
import com.qikserve.supermarket.model.ExpectedTotals;
import com.qikserve.supermarket.model.PromotionType;
import com.qikserve.supermarket.pojo.Product;
import com.qikserve.supermarket.pojo.Promotion;
import com.qikserve.supermarket.service.ProductService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CalculateTotalsHandlerImpl implements CalculateTotalsHandler {
    private final ProductService productService;

    public CalculateTotalsHandlerImpl(@Lazy ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ExpectedTotals calculateExpectedTotals(Basket basket) {
        final List<BasketProduct> basketProducts = basket.getBasketProducts();

        Map<Product, Integer> productQuantityMap = createProductQuantityMap(basketProducts);

        double rawTotal = calculateRawTotal(productQuantityMap);
        double totalPromos = calculateTotalPromos(productQuantityMap);
        double totalPayable = round(rawTotal - totalPromos, 2);

        return ExpectedTotals.builder()
            .rawTotal(rawTotal)
            .totalPromos(totalPromos)
            .totalPayable(totalPayable)
            .basket(basket)
            .build();
    }

    private Map<Product, Integer> createProductQuantityMap(List<BasketProduct> basketProducts) {
        List<Product> products = loadProductsList(basketProducts);
        List<Integer> quantities = loadProductsQuantity(basketProducts);

        return zipToMap(products, quantities);
    }

    private List<Integer> loadProductsQuantity(List<BasketProduct> basketProducts) {
        return basketProducts.stream()
            .map(BasketProduct::getQuantity)
            .toList();
    }

    private List<Product> loadProductsList(List<BasketProduct> basketProducts) {
        return basketProducts.stream()
            .map(basketProduct ->
                productService.getProductById(basketProduct.getPk().getProductId())
            )
            .toList();
    }

    private double calculateTotalPromos(Map<Product, Integer> productQuantityMap) {
        double promosTotal = 0D;
        for (Map.Entry<Product, Integer> map : productQuantityMap.entrySet()) {
            Product product = map.getKey();
            Integer quantity = map.getValue();
            List<Promotion> promotions = product.getPromotions();
            List<Double> calculatedPromos = promotions.stream()
                .map(promotion ->
                    round(PromotionType
                        .valueOf(promotion.getType())
                        .calculatePromo(product, promotion, quantity), 2)).toList();

            promosTotal += calculatedPromos.stream().mapToDouble(promo -> promo).sum();
        }
        return promosTotal;
    }

    private double calculateRawTotal(Map<Product, Integer> productQuantityMap) {
        double rawTotal = 0D;
        for (Map.Entry<Product, Integer> map : productQuantityMap.entrySet()) {
            Product product = map.getKey();
            Integer quantity = map.getValue();
            rawTotal += product.getPrice() * quantity;
        }
        return round(rawTotal, 2);
    }


}
