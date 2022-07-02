package com.example.demo;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {
    private final UserRepo userRepo;
    private final UserEntityFactory factory;

    public UserController(UserRepo repo, UserEntityFactory factory){
        this.userRepo = repo;
        this.factory = factory;
    }

    @GetMapping("/users/{id}")
    EntityModel<StoreUser> getUserById(@PathVariable Long id){
        StoreUser storeUser = userRepo.findById(id).
                orElseThrow(()->new NoSuchElementException());
        return factory.toModel(storeUser);
    }

    @GetMapping("/users")
    CollectionModel<EntityModel<StoreUser>> getAllUsers(){
        List<EntityModel<StoreUser>> users = userRepo.findAll().
                stream().map(user -> factory.toModel(user)).collect(Collectors.toList());
        return CollectionModel.of(users, linkTo(methodOn(UserController.class)
                .getAllUsers()).withSelfRel());
    }

    @PostMapping("/users")
    ResponseEntity<EntityModel<StoreUser>> createUser(@RequestBody StoreUser newStoreUser){
        StoreUser savedStoreUser = userRepo.save(newStoreUser);
        return ResponseEntity.created(linkTo(methodOn(UserController.class).
                getUserById(savedStoreUser.getId())).toUri()).body(factory.toModel(savedStoreUser));
    }
}
