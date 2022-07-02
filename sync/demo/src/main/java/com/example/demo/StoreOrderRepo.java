package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreOrderRepo extends JpaRepository<StoreOrder, Long> {
    List<StoreOrder> getStoreOrdersByStoreUser(StoreUser storeUser);
}
