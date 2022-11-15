package com.qikserve.supermarket.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Promotion {
    private String id;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public double getPrice() {
        return price/100D;
    }

    public int getAmount() {
        return amount;
    }

    private String type;
    @JsonProperty(value = "required_qty")
    private int requiredQuantity;
    @JsonProperty(value = "free_qty")
    private int freeQuantity;
    private double price;
    private int amount;
}
