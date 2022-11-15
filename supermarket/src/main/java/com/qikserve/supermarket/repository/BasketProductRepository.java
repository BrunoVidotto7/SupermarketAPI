package com.qikserve.supermarket.repository;

import com.qikserve.supermarket.model.BasketProduct;
import com.qikserve.supermarket.model.BasketProductPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketProductRepository extends JpaRepository<BasketProduct, BasketProductPK> {
}
