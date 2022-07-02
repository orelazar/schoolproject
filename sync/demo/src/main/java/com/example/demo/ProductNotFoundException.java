package com.example.demo;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long id) {
        super("product with id: " + id + " not found");
    }
}
