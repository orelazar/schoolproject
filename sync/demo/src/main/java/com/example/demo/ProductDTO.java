package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

@Value
@JsonPropertyOrder({"name","price","signature"})
public class ProductDTO {
    @JsonIgnore
    private final Product product;

    public String getName(){
        return this.product.getTitle();
    }

    public Double getPrice(){
        return this.product.getPrice();
    }

    public String getSignature(){
        return "Sold by NadavOrLTD";
    }
}
