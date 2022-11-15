package com.qikserve.supermarket.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    private String id;
    private String name;
    private double price;
    private List<Promotion> promotions;
}
