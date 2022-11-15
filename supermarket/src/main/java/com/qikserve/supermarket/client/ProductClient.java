package com.qikserve.supermarket.client;

import java.util.List;

import com.qikserve.supermarket.client.configuration.ClientConfiguration;
import com.qikserve.supermarket.pojo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${product.service.value}", url = "${product.service.url}", configuration = ClientConfiguration.class)
public interface ProductClient {
    @RequestMapping(method = RequestMethod.GET, value = "/products")
    ResponseEntity<List<Product>> getProducts();

    @RequestMapping(method = RequestMethod.GET, value = "/products/{productId}")
    ResponseEntity<Product> getProductById(@PathVariable("productId") String productId);
}
