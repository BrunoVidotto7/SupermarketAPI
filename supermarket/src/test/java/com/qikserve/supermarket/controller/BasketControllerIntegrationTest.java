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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.qikserve.supermarket.config.WireMockConfig;
import com.qikserve.supermarket.dto.BasketForm;
import com.qikserve.supermarket.dto.BasketProductDto;
import com.qikserve.supermarket.handler.CalculateTotalsHandler;
import com.qikserve.supermarket.mocks.ProductMocks;
import com.qikserve.supermarket.service.BasketProductService;
import com.qikserve.supermarket.service.BasketService;
import com.qikserve.supermarket.service.ExpectedTotalsService;
import com.qikserve.supermarket.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BasketControllerIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private WireMockServer mockProductService;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private BasketService basketService;
    @Autowired
    private BasketProductService basketProductService;
    @Autowired
    private ExpectedTotalsService expectedTotalsService;
    @Autowired
    private CalculateTotalsHandler calculateTotalsHandler;


    @BeforeEach
    void setUp() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        setupAllProductMockResponse(mockProductService);
        setupAmazingPizzaMockResponse(mockProductService);
        setupAmazingBurgerMockResponse(mockProductService);
        setupAmazingSaladMockResponse(mockProductService);
        setupBoringFriesMockResponse(mockProductService);
    }

    @Test
    @Order(1)
    void whenCallCreateMethodPassingAForm_thenABasketIsCreatedLikeExpected() throws Exception {
        BasketForm form = loadBasketForm();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/baskets/create")
            .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8").content(this.mapper.writeValueAsString(form));

        mockMvc.perform(builder).andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(
                    copyToString(
                        BasketControllerIntegrationTest.class.getClassLoader().getResourceAsStream("payload/create-basket-response.json"),
                        Charset.defaultCharset()
                    )
                )
            );
    }

    @Test
    @Order(2)
    void whenCallCheckoutMethodPassingABasketId_thenABasketStatusIsSetToCheckoutLikeExpected() throws Exception {
        Integer id = 1;
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/baskets/checkout/{id}", id);
        mockMvc.perform(builder).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(
                    copyToString(
                        BasketControllerIntegrationTest.class.getClassLoader().getResourceAsStream("payload/checkout-basket-response.json"),
                        Charset.defaultCharset()
                    )
                )
            );
    }

    @Test
    @Order(3)
    void whenCallPayMethodPassingABasketId_thenABasketStatusIsSetToPaidLikeExpected() throws Exception {
        Integer id = 1;
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/baskets/pay/{id}", id);
        mockMvc.perform(builder).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(
                    copyToString(
                        BasketControllerIntegrationTest.class.getClassLoader().getResourceAsStream("payload/paid-basket-response.json"),
                        Charset.defaultCharset()
                    )
                )
            );
    }


    private BasketForm loadBasketForm() {
        BasketForm form = new BasketForm();
        BasketProductDto pizza = BasketProductDto.builder()
            .productId("Dwt5F7KAhi")
            .quantity(4)
            .build();

        BasketProductDto burger = BasketProductDto.builder()
            .productId("PWWe3w1SDU")
            .quantity(3)
            .build();

        BasketProductDto salad = BasketProductDto.builder()
            .productId("C8GDyLrHJb")
            .quantity(5)
            .build();

        BasketProductDto fries = BasketProductDto.builder()
            .productId("4MB7UfpTQs")
            .quantity(1)
            .build();

        form.setProductBaskets(Arrays.asList(pizza, burger, salad, fries));

        return form;
    }

}