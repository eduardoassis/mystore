package com.mystore.store.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mystore.store.model.Product;

import javax.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String type;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "product_id")
    private Product product;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
