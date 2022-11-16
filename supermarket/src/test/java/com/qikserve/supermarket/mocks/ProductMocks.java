package com.qikserve.supermarket.mocks;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.util.StreamUtils.copyToString;

import java.io.IOException;
import java.nio.charset.Charset;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductMocks {
    public static void setupAllProductMockResponse(WireMockServer mockServer) throws IOException {
        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/products"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    copyToString(
                        ProductMocks.class.getClassLoader().getResourceAsStream("payload/get-all-products-response.json"),
                        Charset.defaultCharset()
                    )
                )
            )
        );
    }

    public static void setupAmazingPizzaMockResponse(WireMockServer mockServer) throws IOException {
        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/products/Dwt5F7KAhi"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    copyToString(
                        ProductMocks.class.getClassLoader().getResourceAsStream("payload/get-amazing-pizza-response.json"),
                        Charset.defaultCharset()
                    )
                )
            )
        );
    }

    public static void setupAmazingBurgerMockResponse(WireMockServer mockServer) throws IOException {
        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/products/PWWe3w1SDU"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    copyToString(
                        ProductMocks.class.getClassLoader().getResourceAsStream("payload/get-amazing-burger-response.json"),
                        Charset.defaultCharset()
                    )
                )
            )
        );
    }

    public static void setupAmazingSaladMockResponse(WireMockServer mockServer) throws IOException {
        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/products/C8GDyLrHJb"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    copyToString(
                        ProductMocks.class.getClassLoader().getResourceAsStream("payload/get-amazing-salad-response.json"),
                        Charset.defaultCharset()
                    )
                )
            )
        );
    }

    public static void setupBoringFriesMockResponse(WireMockServer mockServer) throws IOException {
        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/products/4MB7UfpTQs"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    copyToString(
                        ProductMocks.class.getClassLoader().getResourceAsStream("payload/get-boring-fries-response.json"),
                        Charset.defaultCharset()
                    )
                )
            )
        );
    }

    public static void setupNotFoundMockResponse(WireMockServer mockServer) throws IOException {
        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/products/wrongId"))
            .willReturn(aResponse()
                .withStatus(500)));
    }
}
