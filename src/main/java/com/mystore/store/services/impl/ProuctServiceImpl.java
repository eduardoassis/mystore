package com.mystore.store.services.impl;

import com.mystore.store.model.Product;
import com.mystore.store.repository.ImageRepository;
import com.mystore.store.repository.ProductRepository;
import com.mystore.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProuctServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Product create(Product product) {

        Product p = productRepository.save(product);

        Optional.ofNullable(p.getImages()).ifPresent(images -> {
            images.stream().forEach(image -> {
                image.setProduct(p);
                imageRepository.saveAndFlush(image);
            });
        });

        return p;
    }

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
