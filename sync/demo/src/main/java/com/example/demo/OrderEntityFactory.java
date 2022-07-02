package com.example.demo;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderEntityFactory implements RepresentationModelAssembler<StoreOrder, EntityModel<StoreOrder>>{
    @Override
    public EntityModel<StoreOrder> toModel(StoreOrder storeOrder) {
        return EntityModel.of(storeOrder,
                linkTo(methodOn(OrderController.class).getOrderById(storeOrder.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("All Orders"));
    }

    @Override
    public CollectionModel<EntityModel<StoreOrder>> toCollectionModel(Iterable<? extends StoreOrder> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
