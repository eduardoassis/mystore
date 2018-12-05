package com.mystore.store.services;

import com.mystore.store.model.Product;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

public interface ProductService {

    Product create(Product product);
    Product update(Product productWithNewValues) throws EntityNotFoundException;
}
