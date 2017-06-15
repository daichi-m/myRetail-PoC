package com.myretail.product.core;

import com.aerospike.client.AerospikeException;
import com.myretail.product.core.exception.ProductServiceException;
import com.myretail.product.data.ProductDetails;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.io.IOException;

/**
 * The main handler class for the resource which does the external dependency calls.
 */
public class ProductHandler {

    private final DetailsApiFacade detailsApiFacade;
    private final PriceInfoHandler priceInfoHandler;

    @Inject
    public ProductHandler(final DetailsApiFacade detailsApiFacade, final PriceInfoHandler priceInfoHandler) {
        this.detailsApiFacade = detailsApiFacade;
        this.priceInfoHandler = priceInfoHandler;
    }

    /**
     * Gets the details corresponding to the id and fills up a ProductDetails object.
     *
     * @param id The id of the product
     * @return The ProductDetails object produced.
     */
    public ProductDetails getDetails(String id) {
        String name = null;
        String nameError = null;
        Pair<String, Double> priceDetails = null;
        String priceErrorMsg = null;
        try {
            name = detailsApiFacade.getNameForId(id);
        } catch (ProductServiceException ex) {
            nameError = ex.getFailureReason().getMessage();
        }
        try {
            priceDetails = priceInfoHandler.priceForId(id);
        } catch (ProductServiceException ex) {
            priceErrorMsg = ex.getFailureReason().getMessage();
        }
        ProductDetails.ProductDetailsBuilder builder = ProductDetails.builder();
        builder.productId(id);
        if (name != null) {
            builder.name(name);
        } else {
            builder.error(new ProductDetails.Error(nameError));
        }

        if (priceDetails != null) {
            ProductDetails.PriceInfo priceInfo = ProductDetails.PriceInfo.builder().currencyCode(priceDetails.getLeft())
                                                                         .price(priceDetails.getRight()).build();
            builder.priceInfo(priceInfo);
        } else {
            ProductDetails.PriceInfo priceInfo = ProductDetails.PriceInfo.builder()
                                                                         .error(new ProductDetails.Error(priceErrorMsg))
                                                                         .build();
            builder.priceInfo(priceInfo);
        }
        return builder.build();
    }

}
