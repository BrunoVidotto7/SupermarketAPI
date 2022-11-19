package com.qikserve.supermarket.service;

import com.qikserve.supermarket.model.Basket;
import com.qikserve.supermarket.model.ExpectedTotals;

public interface ExpectedTotalsService {
    ExpectedTotals create(ExpectedTotals expectedTotals);

    ExpectedTotals calculateExpectedTotals(Basket basket);
}
