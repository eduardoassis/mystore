package com.mystore.store.unit.model;

import com.mystore.store.model.Image;
import com.mystore.store.model.Product;
import com.mystore.store.repository.ImageRepository;
import com.mystore.store.repository.ProductRepository;
import com.mystore.store.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private ProductService productService;

    private Product productSaved;
    private Product productToDelete;
    private Product parentProduct;
    private Product productToUpdate;

    @Before
    public void beforeAll() {

        productRepository.deleteAll();
        imageRepository.deleteAll();

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

        parentProduct = new Product();

        parentProduct.setName("Parent product");
        parentProduct.setDescription("Product base");
        productRepository.save(parentProduct);
        assertNotNull(parentProduct.getId());

        productToUpdate = new Product();

        productToUpdate.setName("Product with parent");
        productToUpdate.setDescription("Child product");
        productToUpdate.setParent(parentProduct);
        productToUpdate.setImages(new ArrayList<Image>(){{ add(i); }});
        productRepository.save(productToUpdate);
        assertNotNull(parentProduct.getId());

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
    public void shouldCreateProductWithImages() {

        Product product = new Product();

        product.setName("Notebook Sansumg");
        product.setDescription("Laptop Led 11'");

        Image image1 = new Image();
        image1.setType("png");

        Image image2 = new Image();
        image1.setType("jpeg");

        product.setImages(new ArrayList<Image>(){{
            add(image1);
            add(image2);
        }});

        productService.create(product);
        assertNotNull(product.getId());

        assertNotNull(product.getImages());
        assertEquals(2, product.getImages().size());
        assertNotNull(product.getImages().get(0).getId());
        assertNotNull(product.getImages().get(0).getProduct());
        assertNotNull(product.getImages().get(1).getId());
        assertNotNull(product.getImages().get(1).getProduct());
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

    @Test
    public void shouldUpdateProduct() {

        final String newName = "New name";
        final String newDescription = "New description";

        Product product = new Product();
        product.setId(productToUpdate.getId());
        product.setName(newName);
        product.setDescription(newDescription);

        Product p = productService.update(product);

        assertNotNull(p);
        assertEquals(product.getId(), p.getId());
        assertEquals(product.getName(), p.getName());
        assertEquals(product.getDescription(), p.getDescription());
        assertNull(p.getParent());
        assertNull(p.getImages());

    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotUpgradeANonExistentProduct() {

        final String newName = "New name";
        final String newDescription = "New description";

        List<Product> products = productRepository.findAll();

        Product product = new Product();

        // Gets the ID of the last inserted product and increase the value by 1
        product.setId(products.get(products.size()-1).getId() + 1);
        product.setName(newName);
        product.setDescription(newDescription);

        Product p = productService.update(product);

    }
}
