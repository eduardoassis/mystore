package com.mystore.store.services;

import com.mystore.store.model.Product;

import javax.persistence.EntityNotFoundException;

public interface ProductService {
    public Product update(Product productWithNewValues) throws EntityNotFoundException;
}
