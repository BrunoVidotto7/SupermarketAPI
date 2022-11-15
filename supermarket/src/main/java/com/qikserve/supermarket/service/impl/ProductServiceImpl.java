package com.qikserve.supermarket.service.impl;

import java.util.List;

import com.qikserve.supermarket.client.ProductClient;
import com.qikserve.supermarket.error.exception.ResourceNotFoundException;
import com.qikserve.supermarket.error.exception.SupermarketResponseException;
import com.qikserve.supermarket.pojo.Product;
import com.qikserve.supermarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductClient productClient;

    public List<Product> getProducts() {
        ResponseEntity<List<Product>> productsResponse = productClient.getProducts();
        if (productsResponse.getStatusCode().is2xxSuccessful() && productsResponse.hasBody()) {
            return productsResponse.getBody();
        }
        throw new SupermarketResponseException("Error to get response from Supermarket API");
    }

    public Product getProductById(String productId) {
        ResponseEntity<Product> productResponse = productClient.getProductById(productId);
        if (productResponse.getStatusCode().is2xxSuccessful() && productResponse.hasBody()) {
            return productResponse.getBody();
        }
        throw new ResourceNotFoundException("Product id is invalid " + productId);
    }
}
