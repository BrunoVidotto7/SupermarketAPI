package com.qikserve.supermarket.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Setter;

@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    private String id;
    private String name;
    private double price;
    private List<Promotion> promotions;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price/100D;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }
}
