package com.qikserve.supermarket.service.impl;

import com.qikserve.supermarket.handler.CalculateTotalsHandler;
import com.qikserve.supermarket.model.Basket;
import com.qikserve.supermarket.model.ExpectedTotals;
import com.qikserve.supermarket.repository.ExpectedTotalsRepository;
import com.qikserve.supermarket.service.ExpectedTotalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class ExpectedTotalsServiceImpl implements ExpectedTotalsService {

    private final ExpectedTotalsRepository expectedTotalsRepository;
    private final CalculateTotalsHandler calculateTotalsHandler;

    @Override
    public ExpectedTotals create(ExpectedTotals expectedTotals) {
        return expectedTotalsRepository.save(expectedTotals);
    }

    @Override
    public ExpectedTotals calculateExpectedTotals(Basket basket) {
        return calculateTotalsHandler.calculateExpectedTotals(basket);
    }
}
