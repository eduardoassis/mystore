package com.mystore.store.resources;

import com.mystore.store.model.Image;
import com.mystore.store.model.Product;
import com.mystore.store.repository.ImageRepository;
import com.mystore.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Component
public class ImageResource {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    public static final String BASE_URI_RESOURCE = "/images";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listImageByProduct(@PathParam("idProduct") Long id) {
        List<Image> images = imageRepository.findByProductId(id);
        return Response.ok().entity(images).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("idProduct") Long idProduct, @PathParam("id") Long id) {
        Optional<Image> image = imageRepository.findById(id);

        if (!image.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).build();
        }

        imageRepository.delete(image.get());

        return Response.noContent().type(MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("idProduct") Long idProduct, @PathParam("id") Long id, Image image) {

        Optional<Product> p = productRepository.findById(idProduct);
        Optional<Image> i = imageRepository.findById(id);

        if (!p.isPresent()
                || !i.isPresent()
                || i.get().getProduct() == null
                || i.get().getProduct().getId() != idProduct) {

            return Response.status(Response.Status.NOT_FOUND).build();

        }

        Image currentImage = i.get();
        currentImage.setType(image.getType());
        currentImage.setProduct(p.get());

        imageRepository.saveAndFlush(currentImage);

        return Response.ok(currentImage).build();
    }


    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("idProduct") Long idProduct, @PathParam("id") Long id) {

        Optional<Image> image = imageRepository.findById(id);

        if (!image.isPresent()) {
            return Response.status(Response.Status.NO_CONTENT).type(MediaType.APPLICATION_JSON).build();
        }

        return Response.ok(image).type(MediaType.APPLICATION_JSON).build();
    }

}
