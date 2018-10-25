package com.mystore.store.unit.model;

import com.mystore.store.model.Image;
import com.mystore.store.model.Product;
import com.mystore.store.repository.ImageRepository;
import com.mystore.store.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class ProductTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    private Product productSaved = new Product();
    private Product productToDelete = new Product();

    @Before
    public void beforeAll() {


        productSaved = new Product();
        productSaved.setName("Saved product");
        productSaved.setDescription("Product saved before the tests");
        productRepository.save(productSaved);

        Image i = new Image();
        i.setType("png");

        productToDelete = new Product();
        productToDelete.setName("Product to delete");
        productToDelete.setDescription("Product saved before the tests to be deleted");
        productToDelete.setImages(new ArrayList<Image>(){{ add(i); }});
        productRepository.save(productToDelete);
    }

    @Test
    public void shouldCreateProduct() {

        Product product = new Product();

        product.setName("Notebook Sansumg");
        product.setDescription("Laptop Led 11'");
        productRepository.save(product);
        assertNotNull(product.getId());
    }

    @Test
    public void shouldRetrieveSavedSaved() {

        Optional<Product> p = productRepository.findById(productSaved.getId());
        assertTrue(p.isPresent());
        assertNotNull(p.get());

    }

    @Test
    public void shouldDeleteSavedProduct() {

        Long idImage = productToDelete.getImages().get(0).getId();
        Long id = productToDelete.getId();

        assertNotNull(idImage);
        assertNotNull(id);

        productRepository.delete(productToDelete);
        Optional<Product> p = productRepository.findById(id);

        assertFalse(p.isPresent());

        Optional<Image> image = imageRepository.findById(idImage);
        assertFalse(image.isPresent());
    }

    @Test
    public void shouldSaveProductWithParentProduct() {

        Product parentProduct = new Product();

        parentProduct.setName("Parent product");
        parentProduct.setDescription("Product base");
        productRepository.save(parentProduct);
        assertNotNull(parentProduct.getId());

        Product product = new Product();

        product.setName("Product with parent");
        product.setDescription("Child product");
        product.setParent(parentProduct);

        productRepository.save(product);

        assertNotNull(product.getId());

    }

    @Test
    public void shouldCreateProductWithImage() {

        Image image =  new Image();
        image.setType("jpeg");

        Product product = new Product();

        product.setName("Product with parent");
        product.setDescription("Child product");
        product.setImages(new ArrayList<Image>(){{
            add(image);
        }});

        productRepository.save(product);

        assertNotNull(product.getId());
        assertNotNull(product.getImages());
        assertFalse(product.getImages().isEmpty());
        assertNotNull(product.getImages().get(0).getId());

    }

}
