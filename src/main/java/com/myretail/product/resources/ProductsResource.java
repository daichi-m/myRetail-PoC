package com.myretail.product.resources;

import static com.myretail.product.data.PriceUpdateStatus.BAD_REQUEST;
import static com.myretail.product.data.PriceUpdateStatus.UNAVAILABLE;
import static com.myretail.product.data.PriceUpdateStatus.PRICE_UPDATED;

import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.myretail.product.core.ProductHandler;
import com.myretail.product.data.PriceUpdateStatus;
import com.myretail.product.data.ProductDetails;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * The resource class that exposes the HTTP endpoint
 */
@Singleton
@Path("/myRetail/v1/")
@Slf4j
public class ProductsResource {

    @Inject
    ProductHandler handler;

    @GET
    @Path("/products/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name = "getDetailsTimer", absolute = false)
    @Metered(name = "getDetailsMeter", absolute = false)
    public Response getProductDetails(@PathParam("id") String id) {
        ProductDetails details = handler.getDetails(id);
        Response response = Response.status(details.isCompleteInformation() ? Status.OK : Status.PARTIAL_CONTENT)
                                    .entity(details).build();
        log.info("Product Details: {}", details);
        return response;
    }

    @PUT
    @Path("/products/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(name = "putPriceTimer", absolute = false)
    @Metered(name = "putPriceMeter", absolute = false)
    public Response updatePriceDetails(ProductDetails productDetails, @PathParam("id") final String id) {
        PriceUpdateStatus status = handler.updatePriceDetails(id, productDetails);
        log.info("Price Update Status: {}", status);
        if (status.getCode() == PRICE_UPDATED.getCode()) {
            return Response.ok(status).build();
        } else if (status.getCode() == BAD_REQUEST.getCode()) {
            return Response.status(Status.BAD_REQUEST).entity(status).build();
        } else if (status.getCode() == UNAVAILABLE.getCode()) {
            return Response.serverError().entity(status).build();
        } else {
            return Response.ok(status).build();
        }
    }

}
