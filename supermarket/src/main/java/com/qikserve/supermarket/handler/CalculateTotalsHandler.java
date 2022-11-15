package com.qikserve.supermarket.handler;

import com.qikserve.supermarket.model.Basket;
import com.qikserve.supermarket.model.ExpectedTotals;

public interface CalculateTotalsHandler {
    ExpectedTotals calculateExpectedTotals(Basket basket);
}
