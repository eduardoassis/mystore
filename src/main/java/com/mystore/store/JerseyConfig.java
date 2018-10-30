package com.mystore.store;

import com.mystore.store.resources.ProductResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig
{
    public JerseyConfig()
    {
        register(ProductResource.class);
    }
}