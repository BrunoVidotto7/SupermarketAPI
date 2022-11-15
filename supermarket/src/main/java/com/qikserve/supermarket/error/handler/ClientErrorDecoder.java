package com.qikserve.supermarket.error.handler;

import com.qikserve.supermarket.error.exception.SupermarketResponseException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class ClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        return new SupermarketResponseException("Error to get response from Supermarket API");
    }
}
