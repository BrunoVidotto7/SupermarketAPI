package com.qikserve.supermarket.service;

import java.util.List;

import com.qikserve.supermarket.pojo.Product;

public interface ProductService {
    List<Product> getProducts();
    Product getProductById(String productId);

}
