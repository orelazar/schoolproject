package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FakeStoreService {

    private RestTemplate template;

    @Autowired
    public FakeStoreService(RestTemplateBuilder builder){
        this.template = builder.build();
    }

    @Async
    public CompletableFuture<FakeStoreProduct[]> productDetails(){
        String urlTemplate = String.format("https://fakestoreapi.com/products");
        FakeStoreProduct[] products =  template.getForEntity(
                urlTemplate,
                FakeStoreProduct[].class).getBody();
        return CompletableFuture.completedFuture(products);
    }

}
