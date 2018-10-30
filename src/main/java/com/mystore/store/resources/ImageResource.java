package com.mystore.store.resources;

import com.mystore.store.model.Image;
import com.mystore.store.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

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

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("idProduct") Long idProduct, @PathParam("id") Long id) {
        Optional<Image> image = imageRepository.findById(id);

        if (!image.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        imageRepository.delete(image.get());

        return Response.noContent().build();
    }

}
