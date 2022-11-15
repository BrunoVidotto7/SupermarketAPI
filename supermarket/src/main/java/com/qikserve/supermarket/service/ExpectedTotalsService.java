package com.qikserve.supermarket.service;

import com.qikserve.supermarket.model.Basket;
import com.qikserve.supermarket.model.ExpectedTotals;

public interface ExpectedTotalsService {
    ExpectedTotals create(ExpectedTotals expectedTotals);

    public ExpectedTotals calculateExpectedTotals(Basket basket);
}
