package com.example.demo;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class StoreOrder {

    private @Id @GeneratedValue Long id;
    private Double totalPrice;
    private Integer numOfItems;

    @JsonIgnore @ManyToMany
    private List<Product> products = new ArrayList<>();

    @JsonIgnore @ManyToOne
    private StoreUser storeUser;

    public StoreOrder(List<Product> products, StoreUser storeUser){
        this.products = products;
        this.storeUser = storeUser;
        this.totalPrice = getTotalPrice();
        this.numOfItems = getNumOfItems();
    }

    public Double getTotalPrice(){
        Double sum = 0d;
        for(Product p: this.products){
            sum+=p.getPrice();
        }
        return sum;
    }

    public Integer getNumOfItems(){
        return this.products.size();
    }

    public void addProduct(Product product){
        products.add(product);
        this.numOfItems = getNumOfItems();
        this.totalPrice = getTotalPrice();
    }

}
