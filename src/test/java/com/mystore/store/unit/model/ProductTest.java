package com.mystore.store.unit.model;

import com.mystore.store.model.Product;
import com.mystore.store.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class ProductTest {

    @Autowired
    private ProductRepository productRepository;

    private Product productSaved = new Product();
    private Product productToDelete = new Product();

    @Before
    public void beforeAll() {

        productSaved = new Product();
        productSaved.setName("Saved product");
        productSaved.setDescription("Product saved before the tests");
        productRepository.save(productSaved);

        productToDelete = new Product();
        productToDelete.setName("Product to delete");
        productSaved.setDescription("Product saved before the tests to be deleted");
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

        Long id = productToDelete.getId();

        assertNotNull(id);

        productRepository.delete(productToDelete);
        Optional<Product> p = productRepository.findById(id);

        assertFalse(p.isPresent());
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

}
