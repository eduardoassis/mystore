package com.mystore.store.resources;

import com.mystore.store.model.Product;
import com.mystore.store.repository.ProductRepository;
import com.mystore.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "products")
@Component
@Path("/products")
public class ProductResource {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageResource imageResource;

    @Autowired
    private ProductService productService;

    public static final String BASE_URI_RESOURCE = "/products";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection getAllProducts() {
        List products = (List) productRepository.findAll();
        return products;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response findProduct(@PathParam("id") Long id) {

        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            return Response
                    .ok()
                    .entity(product.get())
                    .build();
        }

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {

        Optional<Product> product = productRepository.findById(id);

        if (!product.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        productRepository.delete(product.get());

        return Response.ok().build();
     }

     @POST
     @Consumes(MediaType.APPLICATION_JSON)
     public Response createProduct(Product product) {

        if (product == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Product p = productRepository.save(product);

        return Response.created(URI.create(BASE_URI_RESOURCE + "/" + p.getId())).build();
     }

     @PUT
     @Consumes(MediaType.APPLICATION_JSON)
     @Produces(MediaType.APPLICATION_JSON)
     public Response updateProduct(Product product) {

        if (product == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            Product p = productService.update(product);
            return Response.ok().entity(p).build();
         } catch (EntityNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

     }

     @Path("{idProduct}/images")
     public ImageResource getImageResource() {
        return this.imageResource;
     }

}
