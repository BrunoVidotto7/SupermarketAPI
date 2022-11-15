package com.qikserve.supermarket.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketForm {
    private List<BasketProductDto> productBaskets;
}
