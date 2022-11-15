package com.qikserve.supermarket.repository;

import com.qikserve.supermarket.model.ExpectedTotals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpectedTotalsRepository extends JpaRepository<ExpectedTotals, Long> {
}
