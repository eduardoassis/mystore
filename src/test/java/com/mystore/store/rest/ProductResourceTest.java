package com.mystore.store.rest;

import com.mystore.store.StoreApplication;
import com.mystore.store.model.Image;
import com.mystore.store.model.Product;
import com.mystore.store.repository.ProductRepository;
import com.mystore.store.resources.ProductResource;
import com.mystore.store.services.ProductService;
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
import java.util.List;

import static io.restassured.RestAssured.with;
import static org.junit.Assert.assertNotNull;


@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class ProductResourceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private Product parentProduct, productToUpdate, productWithImages;

    private Long getLastIdPlusOne() {
        List<Product> products = productRepository.findAll();
        return (products.get(products.size() - 1).getId() + 1);
    }

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

        productWithImages = new Product();

        productWithImages.setName("Notebook Sansumg");
        productWithImages.setDescription("Laptop Led 11'");

        Image image1 = new Image();
        image1.setType("png");

        Image image2 = new Image();
        image1.setType("jpeg");

        product.setImages(new ArrayList<Image>(){{
            add(image1);
            add(image2);
        }});

        productService.create(productWithImages);
        assertNotNull(productWithImages.getId());
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
                .request(HttpMethod.POST, ProductResource.BASE_URI_RESOURCE)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    public void shouldNotCreateProduct() {

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .request(HttpMethod.POST, ProductResource.BASE_URI_RESOURCE)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testListAllProducts() {

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(ProductResource.BASE_URI_RESOURCE)
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
                .request(HttpMethod.PUT, ProductResource.BASE_URI_RESOURCE)
                .then()
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(Response.Status.OK.getStatusCode());

    }

    @Test
    public void shouldNotUpdateWithoutSendAProduct() {

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .request(HttpMethod.PUT, ProductResource.BASE_URI_RESOURCE)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());

    }

    @Test
    public void shouldNotUpdateANonExistentProduct() {

        final String newName = "New name";
        final String newDescription = "New description";

        Product product = new Product();
        product.setId(getLastIdPlusOne());
        product.setName(newName);
        product.setDescription(newDescription);
        product.setParent(parentProduct);

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .body(product)
                .when()
                .request(HttpMethod.PUT, ProductResource.BASE_URI_RESOURCE)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());

    }

    @Test
    public void shouldFindAnProductById() {

        final Long id = productToUpdate.getId();

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(ProductResource.BASE_URI_RESOURCE + "/" + id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }

    @Test
    public void shouldNotFindANonExistentProduct() {

        final Long id = getLastIdPlusOne();

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(ProductResource.BASE_URI_RESOURCE + "/" + id)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    }

    @Test
    public void shouldNotDeleteANonExistentProduct() {

        final Long id = getLastIdPlusOne();

        with()
                .delete(ProductResource.BASE_URI_RESOURCE + "/" + id)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void shouldDeleteAnExistentProduct() {

        List<Image> images = productWithImages.getImages();

        with()
                .delete(ProductResource.BASE_URI_RESOURCE + "/" + productWithImages.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}
