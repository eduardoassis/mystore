package com.mystore.store.rest;

import com.mystore.store.StoreApplication;
import com.mystore.store.model.Image;
import com.mystore.store.model.Product;
import com.mystore.store.repository.ProductRepository;
import com.mystore.store.resources.ProductResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;

import static io.restassured.RestAssured.with;
import static org.junit.Assert.assertNotNull;


@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class ProductResourceTest {

    @Autowired
    private ProductRepository productRepository;
    private Product parentProduct, productToUpdate;

    @Before
    public void before() {
        productRepository.deleteAll();
        Product product = new Product();
        product.setDescription("Product test");
        product.setName("Product test");

        productRepository.save(product);

        parentProduct = new Product();

        parentProduct.setName("Parent product");
        parentProduct.setDescription("Product base");
        productRepository.saveAndFlush(parentProduct);
        assertNotNull(parentProduct.getId());

        productToUpdate = new Product();

        productToUpdate.setName("Product with parent");
        productToUpdate.setDescription("Child product");
        productRepository.save(productToUpdate);
        assertNotNull(parentProduct.getId());
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
                .request(HttpMethod.POST, ProductResource.BASE_URI_PRODUCT_RESOURCE)
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

    @Test
    public void shouldUpdateProduct() {


        final String newName = "New name";
        final String newDescription = "New description";

        Product product = new Product();
        product.setId(productToUpdate.getId());
        product.setName(newName);
        product.setDescription(newDescription);
        product.setParent(parentProduct);

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .body(product)
                .when()
                .request(HttpMethod.PUT, ProductResource.BASE_URI_PRODUCT_RESOURCE)
                .then()
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(Response.Status.OK.getStatusCode());

    }
}
