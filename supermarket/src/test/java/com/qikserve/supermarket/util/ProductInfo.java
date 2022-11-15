package com.qikserve.supermarket.util;

public enum ProductInfo {
    BURGER_ID("PWWe3w1SDU"),
    BURGER_NAME("Amazing Burger!"),
    BURGER_PRICE("999"),
    PIZZA_ID("Dwt5F7KAhi"),
    PIZZA_NAME("Amazing Pizza!"),
    PIZZA_PRICE("1099"),
    SALAD_ID("C8GDyLrHJb"),
    SALAD_NAME("Amazing Salad!"),
    SALAD_PRICE("499"),
    FLAT_PERCENT("FLAT_PERCENT"),
    FLAT_PERCENT_ID("Gm1piPn7Fg"),
    FLAT_PERCENT_AMOUNT("10"),
    QTY_BASED_PRICE_OVERRIDE("QTY_BASED_PRICE_OVERRIDE"),
    QTY_BASED_PRICE_OVERRIDE_ID("ibt3EEYczW"),
    QTY_BASED_PRICE_OVERRIDE_REQUIRED_QTY("2"),
    QTY_BASED_PRICE_OVERRIDE_PRICE("1799"),
    BUY_X_GET_Y_FREE("BUY_X_GET_Y_FREE"),
    BUY_X_GET_Y_FREE_ID("ZRAwbsO2qM"),
    BUY_X_GET_Y_FREE_REQUIRED_QTY("2"),
    BUY_X_GET_Y_FREE_QTY("1"),

    ;

    private final String name;

    ProductInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ProductInfo fromString(String productName) {
        for (ProductInfo name : ProductInfo.values()) {
            if (name.name.equalsIgnoreCase(productName)) {
                return name;
            }
        }
        throw new IllegalArgumentException("No constant with text " + productName + " found");
    }
}
