package com.mystore.store.resources;

import com.mystore.store.model.Image;
import com.mystore.store.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
public class ImageResource {

    @Autowired
    private ImageRepository imageRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listImageByProduct(@PathParam("idProduct") Long id) {
        List<Image> images = imageRepository.findByProductId(id);
        return Response.ok().entity(images).build();
    }

}
