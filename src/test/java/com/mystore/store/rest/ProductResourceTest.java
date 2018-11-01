package com.mystore.store.rest;

import com.mystore.store.StoreApplication;
import com.mystore.store.model.Product;
import com.mystore.store.repository.ProductRepository;
import com.mystore.store.resources.ProductResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.with;


@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class ProductResourceTest {

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void before() {
        productRepository.deleteAll();
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
}
