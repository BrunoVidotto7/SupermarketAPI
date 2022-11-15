package com.qikserve.supermarket.model;

import com.qikserve.supermarket.pojo.Product;
import com.qikserve.supermarket.pojo.Promotion;
import com.qikserve.supermarket.util.SupermarketUtil;

public enum PromotionType {
    FLAT_PERCENT {
        @Override
        public double calculatePromo(Product product, Promotion promotion, int quantity) {
            final double totalPrice = SupermarketUtil.convertPennyToPound(product.getPrice()) * quantity;
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
                final double priceWithDiscount = quotient * SupermarketUtil.convertPennyToPound(promotion.getPrice()) + reminder * product.getPrice();
                final double totalPrice = SupermarketUtil.convertPennyToPound(product.getPrice()) * quantity;
                return totalPrice - priceWithDiscount;
            }
            return 0D;
        }
    },
    BUY_X_GET_Y_FREE {
        @Override
        public double calculatePromo(Product product, Promotion promotion, int quantity) {
            if (quantity >= promotion.getRequiredQuantity()) {
                double productPrice = SupermarketUtil.convertPennyToPound(product.getPrice());
                final int reminder = quantity % promotion.getRequiredQuantity();
                final int quotient = quantity / promotion.getRequiredQuantity();
                final double totalPrice = productPrice * quantity;
                final double priceWithDiscount = quotient * productPrice + reminder * productPrice;
                return totalPrice - priceWithDiscount;
            }
            return 0D;
        }
    };

    public abstract double calculatePromo(Product product, Promotion promotion, int quantity);
}
