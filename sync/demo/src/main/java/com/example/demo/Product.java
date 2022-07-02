package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Product  implements Comparable<Product>{
    private @Id @GeneratedValue Long id;

    @Override
    public int compareTo(Product o) {
        return Double.compare(this.getPrice(), o.getPrice());
    }

    private String title;
    private String category;
    private double price;

    @JsonIgnore @ManyToMany(mappedBy = "products")
    private List<StoreOrder> storeOrders = new ArrayList<>();

    public Product(){}

    public Product(String title, String category, double price){
        this.category = category;
        this.price = price;
        this.title = title;
    }
}
