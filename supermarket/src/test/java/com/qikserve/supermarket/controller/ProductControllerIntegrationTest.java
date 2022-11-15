package com.qikserve.supermarket.controller;

import static com.qikserve.supermarket.mocks.ProductMocks.setupAllProductMockResponse;
import static com.qikserve.supermarket.mocks.ProductMocks.setupAmazingBurgerMockResponse;
import static com.qikserve.supermarket.mocks.ProductMocks.setupAmazingPizzaMockResponse;
import static com.qikserve.supermarket.mocks.ProductMocks.setupAmazingSaladMockResponse;
import static com.qikserve.supermarket.mocks.ProductMocks.setupBoringFriesMockResponse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StreamUtils.copyToString;

import java.io.IOException;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.qikserve.supermarket.config.WireMockConfig;
import com.qikserve.supermarket.dto.BasketForm;
import com.qikserve.supermarket.handler.CalculateTotalsHandler;
import com.qikserve.supermarket.service.BasketProductService;
import com.qikserve.supermarket.service.BasketService;
import com.qikserve.supermarket.service.ExpectedTotalsService;
import com.qikserve.supermarket.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WireMockConfig.class})
class ProductControllerIntegrationTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private WireMockServer mockProductService;


    @BeforeEach
    void setUp() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        setupAllProductMockResponse(mockProductService);
        setupAmazingPizzaMockResponse(mockProductService);
    }

    @Test
    void whenCallGetProducts_thenGetProducts() throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/products");

        mockMvc.perform(builder).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(
                    copyToString(
                        BasketControllerIntegrationTest.class.getClassLoader().getResourceAsStream("payload/get-all-products-response.json"),
                        Charset.defaultCharset()
                    )
                )
            );
    }

    @Test
    void whenCallGetProductById_thenGetProduct() throws Exception {
        String productId = "Dwt5F7KAhi";

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/products/{productId}", productId);

        mockMvc.perform(builder).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(
                    copyToString(
                        BasketControllerIntegrationTest.class.getClassLoader().getResourceAsStream("payload/get-amazing-pizza-response.json"),
                        Charset.defaultCharset()
                    )
                )
            );
    }
}