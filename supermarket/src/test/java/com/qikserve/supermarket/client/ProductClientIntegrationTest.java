package com.qikserve.supermarket.client;

import static com.qikserve.supermarket.mocks.ProductMocks.setupAllProductMockResponse;
import static com.qikserve.supermarket.mocks.ProductMocks.setupAmazingPizzaMockResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.qikserve.supermarket.config.WireMockConfig;
import com.qikserve.supermarket.error.exception.SupermarketResponseException;
import com.qikserve.supermarket.pojo.Product;
import com.qikserve.supermarket.util.SupermarketUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WireMockConfig.class })
class ProductClientIntegrationTest {

    @Autowired
    private WireMockServer mockProductService;

    @Autowired
    private ProductClient productClient;

    @BeforeEach
    void setUp() throws IOException {
        setupAllProductMockResponse(mockProductService);
        setupAmazingPizzaMockResponse(mockProductService);
    }

    @Test
    void whenGetAllProducts_thenTheListIsNotEmpty() {
        assertFalse(Objects.requireNonNull(productClient.getProducts().getBody()).isEmpty());
    }

    @Test
    void whenGetAllProducts_thenReturnListWithExpectedElements() {
        ResponseEntity<List<Product>> productResponseEntity = productClient.getProducts();
        assertTrue(productResponseEntity.hasBody() && productResponseEntity.getStatusCode() == HttpStatus.OK);

        List<Product> products = productClient.getProducts().getBody();
        assert products != null;

        Product pizza = products.get(0);
        Product burger = products.get(1);

        assertEquals("Dwt5F7KAhi", pizza.getId());
        assertEquals("Amazing Pizza!", pizza.getName());
        assertEquals(10.99, pizza.getPrice());

        assertEquals("PWWe3w1SDU", burger.getId());
        assertEquals("Amazing Burger!", burger.getName());
        assertEquals(9.99, burger.getPrice());
    }

    @Test
    void whenGetProductById_thenReturnAProjectAsExpected() {
        ResponseEntity<Product> productResponseEntity = productClient.getProductById("Dwt5F7KAhi");
        assertTrue(productResponseEntity.hasBody() && productResponseEntity.getStatusCode() == HttpStatus.OK);
        Product pizza = productResponseEntity.getBody();
        assert pizza != null;
        assertEquals("Dwt5F7KAhi", pizza.getId());
        assertEquals("Amazing Pizza!", pizza.getName());
        assertEquals(10.99, SupermarketUtil.convertPennyToPound(pizza.getPrice()));
    }

    @Test
    void whenTryToGetProductPassingAWrongId_thenThrowNewSuperMarketException() {
        Exception exception = assertThrows(SupermarketResponseException.class, () -> {
            productClient.getProductById("wrongId");
        });
        assertEquals("Error to get response from Supermarket API", exception.getMessage());

    }
}