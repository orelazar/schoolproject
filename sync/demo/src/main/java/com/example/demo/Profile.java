package com.example.demo;

import java.time.LocalDateTime;
import java.util.UUID;

public class Profile {

    private UUID id;
    private String name;
    private Double age;
    private LocalDateTime creationDate;

    public Profile(String name, Double age){
        this.creationDate = LocalDateTime.now();
        this.id = UUID.randomUUID();
        this.age = age;
        this.name = name;
    }
}
