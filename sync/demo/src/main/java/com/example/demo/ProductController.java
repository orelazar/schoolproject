package com.example.demo;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {
    private final ProductRepo productRepo;
    private final ProductEntityFactory factory;
    private ProductDTOFactory dtoFactory;

    public ProductController(ProductRepo repo, ProductEntityFactory factory, ProductDTOFactory dtoFactory){
        this.productRepo = repo;
        this.factory = factory;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/products/{id}")
    EntityModel<Product> getProductById(@PathVariable Long id){
        Product product = productRepo.findById(id).
                orElseThrow(()->new ProductNotFoundException(id));
        return factory.toModel(product);
    }

    @GetMapping("/products")
    CollectionModel<EntityModel<Product>> getAllProducts(){
        List<EntityModel<Product>> products = productRepo.findAll().
                stream().map(product -> factory.toModel(product)).collect(Collectors.toList());
        return CollectionModel.of(products, linkTo(methodOn(ProductController.class)
                .getAllProducts()).withSelfRel());
    }

    @PostMapping("/products")
    ResponseEntity<EntityModel<Product>> createProduct(@RequestBody Product newProduct){
        Product savedProduct = productRepo.save(newProduct);
        return ResponseEntity.created(linkTo(methodOn(ProductController.class).
                getProductById(savedProduct.getId())).toUri()).body(factory.toModel(savedProduct));
    }

    @GetMapping("products/sale")
    CollectionModel<EntityModel<Product>> getProductsInSale(@RequestParam(required = true)  Double price){
        List<EntityModel<Product>> products =  productRepo.findAll().stream().
                filter(product -> product.getPrice() < price)
                .sorted().map(factory::toModel).collect(Collectors.toList());
        return CollectionModel.of(products, linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());

    }
    @GetMapping("products/category")
    CollectionModel<EntityModel<Product>> getProductsByCategory(@RequestParam(required = true)  String category){
        List<EntityModel<Product>> products =  productRepo.getProductsByCategory(category).stream()
                .map(factory::toModel).collect(Collectors.toList());
        return CollectionModel.of(products, linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());

    }

    @GetMapping("/products/byname/{name}")
    ResponseEntity<?> getProductById(@PathVariable String name){
        List<EntityModel<Product>> products = productRepo.getProductsByTitle(name)
                        .stream().map(factory::toModel).collect(Collectors.toList());
        return ResponseEntity.ok().body(CollectionModel.of(products));
    }

    @PutMapping("/products/{id}")
    ResponseEntity<EntityModel<Product>> updatePrice(@PathVariable Long id, @RequestBody(required = true)  Double newPrice){
        EntityModel<Product> product = productRepo.findById(id).map(productToUpdate->{
            productToUpdate.setPrice(newPrice);
            return productRepo.save(productToUpdate);
        }).map(factory::toModel).orElseThrow(()->new ProductNotFoundException(id));
        return ResponseEntity.accepted().body(product);
    }

    @DeleteMapping("products/{id}")
    ResponseEntity<?> deleteOrder(@PathVariable Long id){
        productRepo.delete(productRepo.findById(id).orElseThrow(()->new OrderNotFoundException(id)));
        return ResponseEntity.ok("product with id: " + id+ " was deleted");
    }

    @GetMapping("products/{id}/info")
    public ResponseEntity<EntityModel<ProductDTO>> productInfo(@PathVariable Long id){
        return productRepo.findById(id).map(ProductDTO::new)
                .map(dtoFactory::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("products/info")
    public ResponseEntity<CollectionModel<EntityModel<ProductDTO>>> allProductsInfo(){
        return ResponseEntity.ok(dtoFactory.toCollectionModel(StreamSupport.stream(productRepo.findAll().spliterator(),
                false).map(ProductDTO::new)
                .collect(Collectors.toList())));
    }
}
