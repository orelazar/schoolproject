package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> getProductsByTitle(String name);
    List<Product> getProductsByCategory(String name);
}
