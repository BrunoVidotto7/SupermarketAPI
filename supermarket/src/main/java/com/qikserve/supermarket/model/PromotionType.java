package com.qikserve.supermarket.model;

import com.qikserve.supermarket.pojo.Product;
import com.qikserve.supermarket.pojo.Promotion;

public enum PromotionType {
    FLAT_PERCENT {
        @Override
        public double calculatePromo(Product product, Promotion promotion, int quantity) {
            final double totalPrice = product.getPrice() * quantity;
            final double amount = promotion.getAmount() / 100.0;
            return totalPrice * amount;
        }
    },
    QTY_BASED_PRICE_OVERRIDE {
        @Override
        public double calculatePromo(Product product, Promotion promotion, int quantity) {
            if (quantity >= promotion.getRequiredQuantity()) {
                final int reminder = quantity % promotion.getRequiredQuantity();
                final int quotient = quantity / promotion.getRequiredQuantity();
                final double priceWithDiscount = quotient * promotion.getPrice() + reminder * product.getPrice();
                final double totalPrice = product.getPrice() * quantity;
                return totalPrice - priceWithDiscount;
            }
            return 0D;
        }
    },
    BUY_X_GET_Y_FREE {
        @Override
        public double calculatePromo(Product product, Promotion promotion, int quantity) {
            if (quantity >= promotion.getRequiredQuantity()) {
                final int reminder = quantity % promotion.getRequiredQuantity();
                final int quotient = quantity / promotion.getRequiredQuantity();
                final double totalPrice = product.getPrice() * quantity;
                final double priceWithDiscount = quotient * product.getPrice() + reminder * product.getPrice();
                return totalPrice - priceWithDiscount;
            }
            return 0D;
        }
    };

    public abstract double calculatePromo(Product product, Promotion promotion, int quantity);
}
