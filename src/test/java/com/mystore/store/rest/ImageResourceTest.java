package com.mystore.store.rest;

import com.mystore.store.StoreApplication;
import com.mystore.store.model.Image;
import com.mystore.store.model.Product;
import com.mystore.store.repository.ImageRepository;
import com.mystore.store.repository.ProductRepository;
import com.mystore.store.resources.ImageResource;
import com.mystore.store.resources.ProductResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.with;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class ImageResourceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    private Product product;
    private Image image;

    @Before
    public void before() {
        productRepository.deleteAll();
        product = new Product();
        product.setDescription("Product test");
        product.setName("Product test");

        Image i = new Image();
        i.setType("png");
        i.setProduct(product);

        image = new Image();
        image.setType("png");
        image.setProduct(product);

        product.setImages(new ArrayList<Image>(){{
            add(i);
            add(image);
        }});
        productRepository.saveAndFlush(product);
        imageRepository.saveAndFlush(i);
        imageRepository.saveAndFlush(image);
    }

    @Test
    public void testListAllImagesByProduct() {

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(ProductResource.BASE_URI_RESOURCE + "/" + product.getId() + ImageResource.BASE_URI_RESOURCE)
                .then()
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(Response.Status.OK.getStatusCode());

    }

    @Test
    public void shouldDeleteImage() {

        Image image = product.getImages().get(0);

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(ProductResource.BASE_URI_RESOURCE + "/" + product.getId() + ImageResource.BASE_URI_RESOURCE + "/" + image.getId() )
                .then()
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void shouldFailWhenTryToDeleteAnInexistentImage() {

        Long idImage = 10000l;

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(ProductResource.BASE_URI_RESOURCE + "/" + product.getId() + ImageResource.BASE_URI_RESOURCE + "/" + idImage )
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void shouldUpdateAnImage() {

        final String newType = "test";
        Image image = product.getImages().get(0);
        image.setType(newType);

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .body(image)
                .when()
                .put(ProductResource.BASE_URI_RESOURCE + "/" + product.getId() + ImageResource.BASE_URI_RESOURCE + "/" + image.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

        Optional<Image> i = imageRepository.findById(image.getId());
        assertTrue(i.isPresent());
        assertEquals(newType, i.get().getType());
    }

    @Test
    public void shouldNotUpdateANonExistentImage() {

        final String newType = "test";
        Image image = product.getImages().get(0);
        image.setType(newType);
        final Long id = getLastIdPlusOne();

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .body(image)
                .when()
                .put(ProductResource.BASE_URI_RESOURCE + "/" + product.getId() + ImageResource.BASE_URI_RESOURCE + "/" + id)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

        Optional<Image> i = imageRepository.findById(image.getId());
        assertTrue(i.isPresent());
        assertNotEquals(newType, i.get().getType());
    }

    @Test
    public void shouldFindAnImageById() {

        final Long id = image.getId();
        final Long productId = image.getProduct().getId();

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(ProductResource.BASE_URI_RESOURCE + "/" + productId + ImageResource.BASE_URI_RESOURCE + "/" + id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }

    @Test
    public void shouldNotFindANonExistentImageById() {

        final Long id = getLastIdPlusOne();
        final Long productId = image.getProduct().getId();

        with()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(ProductResource.BASE_URI_RESOURCE + "/" + productId + ImageResource.BASE_URI_RESOURCE + "/" + id)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    }

    private Long getLastIdPlusOne() {
        List<Image> images = imageRepository.findAll();
        return (images.get(images.size() - 1).getId() + 1);
    }
}
