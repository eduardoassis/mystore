package com.mystore.store.services.impl;

import com.mystore.store.model.Product;
import com.mystore.store.repository.ProductRepository;
import com.mystore.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Component
public class ProuctServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product update(Product productWithNewValues) throws EntityNotFoundException{

        Optional<Product> currentProduct = productRepository.findById(productWithNewValues.getId());

        if (!currentProduct.isPresent()) {
            throw new EntityNotFoundException();
        }

        Product p = currentProduct.get();

        p.setName(productWithNewValues.getName());
        p.setDescription(productWithNewValues.getDescription());
        p.setImages(productWithNewValues.getImages());
        p.setParent(productWithNewValues.getParent());

        return productRepository.saveAndFlush(p);
    }
}
