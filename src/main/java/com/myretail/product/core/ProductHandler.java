package com.myretail.product.core;

import com.myretail.product.data.ProductDetails;
import com.myretail.product.db.PriceDao;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;

/**
 * The main handler class for the resource which does the external dependency calls.
 */
public class ProductHandler {

    private final DetailsApiFacade detailsApiFacade;
    private final PriceDao priceDao;

    @Inject
    public ProductHandler(final DetailsApiFacade detailsApiFacade, final PriceDao priceDao) {
        this.detailsApiFacade = detailsApiFacade;
        this.priceDao = priceDao;
    }

    public ProductDetails getDetails(String id) {
        String name = detailsApiFacade.getNameForId(id);
        Pair<String, Double> priceDetails = priceDao.priceForId(id);
        ProductDetails.PriceInfo priceInfo = ProductDetails.PriceInfo.builder().price(priceDetails.getRight())
                                                                     .currencyCode(priceDetails.getLeft()).build();
        ProductDetails details = ProductDetails.builder().productId(id).name(name).priceInfo(priceInfo).build();
        return details;
    }

}
