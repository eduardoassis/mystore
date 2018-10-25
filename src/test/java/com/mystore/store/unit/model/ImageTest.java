package com.mystore.store.unit.model;

import com.mystore.store.model.Image;
import com.mystore.store.repository.ImageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class ImageTest {

    @Autowired
    private ImageRepository imageRepository;

    private Long idSavedImage;
    private Image imageToDelete;


    @Before
    public void before() {
        Image i = new Image();
        i.setType("jpeg");
        imageRepository.save(i);
        assertNotNull(i.getId());
        idSavedImage = i.getId();

        imageToDelete = new Image();
        imageToDelete.setType("jpeg");
        imageRepository.save(imageToDelete);
    }

    @Test
    public void shouldSaveImage() {

        Image image = new Image();
        image.setType("png");
        imageRepository.save(image);
        assertNotNull(image.getId());

    }

    @Test
    public void shouldRetrieveImage() {
        Optional<Image> image = imageRepository.findById(idSavedImage);
        assertTrue(image.isPresent());
        assertEquals(idSavedImage, image.get().getId());
    }

    @Test
    public void shouldDeleteImage() {
        Long id = imageToDelete.getId();
        imageRepository.delete(imageToDelete);

        Optional<Image> image = imageRepository.findById(id);
        assertFalse(image.isPresent());
    }
}
