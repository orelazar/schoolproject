package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakeStoreProduct {

    private Long id;
    private String title;
    private Double price;
    private String category;

    @Override
    public String toString(){
        return "Fake store Product:\nProduct id: "+ this.getId()+" Product name: "+ this.getTitle()+
                " category: " + this.getCategory()+ " price: " +this.getPrice()+ "\n";
    }

}
