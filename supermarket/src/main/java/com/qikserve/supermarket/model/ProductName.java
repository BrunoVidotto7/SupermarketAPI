package com.qikserve.supermarket.model;

import java.util.List;
import java.util.stream.Collectors;

import com.qikserve.supermarket.pojo.Product;

public enum ProductName {

    AMAZING_PIZZA("Amazing Pizza!"),
    AMAZING_BURGER("Amazing Burger!"),
    AMAZING_SALAD("Amazing Salad!"),
    BORING_FRIES("Boring Fries!");

    private final String name;

    ProductName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ProductName fromString(String productName) {
        for (ProductName name : ProductName.values()) {
            if (name.name.equalsIgnoreCase(productName)) {
                return name;
            }
        }
        throw new IllegalArgumentException("No constant with text " + productName + " found");
    }
}
