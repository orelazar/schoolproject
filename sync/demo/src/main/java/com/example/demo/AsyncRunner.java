package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class AsyncRunner implements CommandLineRunner {

    private final FakeStoreService fakeStoreService;
    private final ProductRepo repo;

    public AsyncRunner(@Lazy FakeStoreService fakeStoreService, ProductRepo repo){
        this.fakeStoreService = fakeStoreService;
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        CompletableFuture<FakeStoreProduct[]> futureProducts = fakeStoreService.productDetails();

        List<CompletableFuture<FakeStoreProduct>> taskArray = new ArrayList<>();
        CompletableFuture.allOf(futureProducts).join();

        FakeStoreProduct[] products = futureProducts.get();
        for(FakeStoreProduct p: products){
            repo.save(new Product(p.getTitle(),p.getCategory(),p.getPrice()));
        }
    }
}
