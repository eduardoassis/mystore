package com.mystore.store.controllers;

import com.mystore.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "products")
@Component
@Path("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection getAllProducts() {
        List products = (List) productRepository.findAll();
        return products;
    }
}
