package com.mystore.store.unit.model;

import com.mystore.store.model.Image;
import com.mystore.store.repository.ImageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class ImageTest {

    @Autowired
    private ImageRepository imageRepository;

    private Long id;

    @Test
    public void shouldSaveImage() {

        Image image = new Image();
        image.setType("png");
        imageRepository.save(image);
        assertNotNull(image.getId());

    }
}
