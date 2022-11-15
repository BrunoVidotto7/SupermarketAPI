package com.qikserve.supermarket.client.configuration;

import com.qikserve.supermarket.error.handler.ClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class ClientConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ClientErrorDecoder();
    }
}
