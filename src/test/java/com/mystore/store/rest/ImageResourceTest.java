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

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;

import static io.restassured.RestAssured.with;
import static org.junit.Assert.assertNotNull;


@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class ImageResourceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    private Product product, productToUpdate, productToDelete;
    private Image imageToUpdate, imageToDelete;

    @Before
    public void before() {
        productRepository.deleteAll();
        product = new Product();
        product.setDescription("Product test");
        product.setName("Product test");

        Image image = new Image();
        image.setType("png");
        image.setProduct(product);

        product.setImages(new ArrayList<Image>(){{
            add(image);
        }});
        productRepository.saveAndFlush(product);
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

}
