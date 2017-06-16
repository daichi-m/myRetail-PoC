package com.myretail.product.core;

import com.myretail.product.core.exception.ProductServiceException;
import com.myretail.product.data.PriceUpdateStatus;
import com.myretail.product.data.ProductDetails;
import com.myretail.product.data.ProductDetails.Error;
import com.myretail.product.data.ProductDetails.PriceInfo;
import com.myretail.product.data.ProductDetails.ProductDetailsBuilder;
import com.myretail.product.data.ProductDetails.PriceInfo.PriceInfoBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

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
        ProductDetailsBuilder builder = ProductDetails.builder();
        builder.productId(id).completeInformation(true);
        if (name != null) {
            builder.name(name);
        } else {
            builder.error(new Error(nameError)).completeInformation(false);
        }

        if (priceDetails != null) {
            PriceInfo priceInfo = new PriceInfo().builder().currencyCode(priceDetails.getLeft())
                                                 .price(priceDetails.getRight()).build();
            builder.priceInfo(priceInfo);
        } else {
            PriceInfo priceInfo = new PriceInfo().builder().error(new Error(priceErrorMsg)).build();
            builder.priceInfo(priceInfo).completeInformation(false);
        }
        return builder.build();
    }

    public PriceUpdateStatus updatePriceDetails(final ProductDetails productDetails) {
        if (productDetails == null || productDetails.getPriceInfo() == null) {
            return PriceUpdateStatus.BAD_REQUEST;
        }
        String id = productDetails.getProductId();
        String currency = productDetails.getPriceInfo().getCurrencyCode();
        Double price = productDetails.getPriceInfo().getPrice();
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(currency) || price == null) {
            return PriceUpdateStatus.BAD_REQUEST;
        }

        try {
            priceInfoHandler.updateOrInsertPrice(id, currency, price);
        } catch (ProductServiceException ex) {
            return PriceUpdateStatus.DB_ERROR;
        }
        return PriceUpdateStatus.PRICE_UPDATED;
    }

}
