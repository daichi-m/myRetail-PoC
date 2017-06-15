package com.myretail.product.resources;

import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.myretail.product.core.ProductHandler;
import com.myretail.product.data.ProductDetails;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The resource class that exposes the HTTP endpoint
 */
@Singleton
@Path("/myRetail/v1/")
public class ProductsResource {

    @Inject
    ProductHandler handler;

    @GET
    @Path("/products/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name = "getDetailsTimer", absolute = false)
    @Metered(name = "getDetailsMeter", absolute = false)
    public ProductDetails getProductDetails(@PathParam("id") String id) {
        return handler.getDetails(id);
    }

}
