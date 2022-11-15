package com.qikserve.supermarket.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Promotion {
    private String id;
    private String type;
    @JsonProperty(value = "required_qty")
    private int requiredQuantity;
    @JsonProperty(value = "free_qty")
    private int freeQuantity;
    private double price;
    private int amount;
}
