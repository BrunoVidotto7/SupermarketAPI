package com.qikserve.supermarket.error.exception;

public class BasketAlreadyClosedException extends RuntimeException {
    public BasketAlreadyClosedException() {
        super("This Basket is already closed.");
    }
}
