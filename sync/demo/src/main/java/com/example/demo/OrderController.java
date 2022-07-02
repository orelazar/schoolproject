package com.example.demo;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {
    private final StoreOrderRepo storeOrderRepo;
    private final OrderEntityFactory factory;
    private final UserRepo userRepo;

    public OrderController(StoreOrderRepo repo, OrderEntityFactory factory, UserRepo userRepo) {
        this.storeOrderRepo = repo;
        this.factory = factory;
        this.userRepo = userRepo;
    }

    @GetMapping("/orders")
    CollectionModel<EntityModel<StoreOrder>> getAllOrders() {
        List<EntityModel<StoreOrder>> orders = storeOrderRepo.findAll().
                stream().map(order -> factory.toModel(order)).collect(Collectors.toList());
        return CollectionModel.of(orders, linkTo(methodOn(ProductController.class)
                .getAllProducts()).withSelfRel());
    }

    @GetMapping("/orders/user")
    CollectionModel<EntityModel<StoreOrder>> getOrdersByUser(@RequestBody StoreUser storeUser) {
        List<EntityModel<StoreOrder>> orders = storeOrderRepo.getStoreOrdersByStoreUser(storeUser).
                stream().map(order -> factory.toModel(order)).collect(Collectors.toList());
        return CollectionModel.of(orders, linkTo(methodOn(ProductController.class)
                .getAllProducts()).withSelfRel());
    }

    @GetMapping("/orders/id")
    EntityModel<StoreOrder> getOrderById(@PathVariable Long id) {
        StoreOrder storeOrder = storeOrderRepo.findById(id).orElseThrow(
                () -> new OrderNotFoundException(id));
        return factory.toModel(storeOrder);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<StoreOrder>> createOrder(@RequestBody(required = true) Product product, @RequestBody Long id) {
        List<Product> products = new ArrayList<>();
        products.add(product);
        StoreUser storeUser = userRepo.findById(id).orElseThrow(()-> new RuntimeException("user doesnt exist"));
        StoreOrder storeOrder = storeOrderRepo.save(new StoreOrder(products, storeUser));
        return ResponseEntity.created(linkTo(methodOn(OrderController.class).
                getOrderById(storeOrder.getId())).toUri()).body(factory.toModel(storeOrder));

    }

    @PutMapping("orders/{id}")
    ResponseEntity<?> addProductToOrder(@PathVariable Long id, @RequestBody Product product){
        StoreOrder storeOrder = storeOrderRepo.findById(id).map(orderToUpdate ->{
            orderToUpdate.addProduct(product);
            return storeOrderRepo.save(orderToUpdate);}).orElseThrow(()-> new OrderNotFoundException(id));
        return ResponseEntity.created(linkTo(methodOn(OrderController.class).
                getOrderById(storeOrder.getId())).toUri()).body(factory.toModel(storeOrder));
    }

    @DeleteMapping("orders/{id}")
    ResponseEntity<?> deleteOrder(@PathVariable Long id){
        storeOrderRepo.delete(storeOrderRepo.findById(id).orElseThrow(()->new OrderNotFoundException(id)));
        return ResponseEntity.ok("order with id: " + id+ " was deleted");
    }
}