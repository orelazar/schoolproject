//package com.example.demo;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class SeedDB {
//    private static final Logger logger = LoggerFactory.getLogger(SeedDB.class);
//
//    @Bean
//    CommandLineRunner startDB(ProductRepo myProducts){
//        return args -> {
//            //TODO: seed DB from api
//            logger.debug("seeding DB from 3rd party api");
//            myProducts.save(
//                    new Product("product","category",100)
//            );
//        };
//    }
//}
