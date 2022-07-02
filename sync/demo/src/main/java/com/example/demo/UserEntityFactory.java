package com.example.demo;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserEntityFactory implements RepresentationModelAssembler<StoreUser, EntityModel<StoreUser>> {

    @Override
    public EntityModel<StoreUser> toModel(StoreUser storeUser) {
        return EntityModel.of(storeUser,
                linkTo(methodOn(ProductController.class).getProductById(storeUser.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAllProducts()).withRel("back to all users"));
    }

    @Override
    public CollectionModel<EntityModel<StoreUser>> toCollectionModel(Iterable<? extends StoreUser> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
