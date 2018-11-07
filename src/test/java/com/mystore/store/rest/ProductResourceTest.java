package com.mystore.store.rest;

import com.mystore.store.StoreApplication;
import com.mystore.store.model.Product;
import com.mystore.store.repository.ProductRepository;
import com.mystore.store.resources.ProductResource;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.with;


@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class ProductResourceTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void beforeEach() {
        productRepository.deleteAll();

        Product product = new Product();
        product.setDescription("Product test");
        product.setName("Product test");

        productRepository.save(product);
    }

    @Test
    public void testCreateProduct() {

        Product product = new Product();
        product.setDescription("Product test");
        product.setName("Product test");

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .body(product)
                .when()
                .request("POST", ProductResource.BASE_URI_PRODUCT_RESOURCE)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    public void testListAllProducts() {

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(ProductResource.BASE_URI_PRODUCT_RESOURCE)
                .then()
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(Response.Status.OK.getStatusCode());

    }
}
