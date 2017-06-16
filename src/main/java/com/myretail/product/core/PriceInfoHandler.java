package com.myretail.product.core;

import static com.myretail.product.core.exception.ProductServiceException.FailureReason.DB_ERROR;
import static com.myretail.product.core.exception.ProductServiceException.FailureReason.PRICE_NOT_FOUND;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.myretail.product.ProductsServiceConfiguration;
import com.myretail.product.core.exception.ProductServiceException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;

/**
 * DAO class to communicate to Aerospike for price related information.
 */
public class PriceInfoHandler {

    private static final String CURRENCY_BIN = "curr";
    private static final String VALUE_BIN = "val";
    private final AerospikeClient aerospikeClient;
    private final String namespace;
    private final String priceSet;

    @Inject
    public PriceInfoHandler(final AerospikeClient aerospikeClient, final ProductsServiceConfiguration configuration) {
        this.aerospikeClient = aerospikeClient;
        this.namespace = configuration.getAerospikeNamespace();
        this.priceSet = configuration.getAerospikePriceSet();
    }

    /**
     * For a given id, returns the price related information from Aerospike DB.
     *
     * @param id The id of the product
     * @return A Pair of (Currency, Price) info
     * @throws ProductServiceException In case the details is not found, or DB is unreachable.
     */
    public Pair<String, Double> priceForId(String id) throws ProductServiceException {
        Key asKey = new Key(namespace, priceSet, id);
        Record record = null;
        try {
            record = aerospikeClient.get(null, asKey);
        } catch (AerospikeException ae) {
            throw new ProductServiceException(DB_ERROR);
        }
        if (record != null) {
            String currency = record.getString(CURRENCY_BIN);
            Double value = record.getDouble(VALUE_BIN);
            return ImmutablePair.of(currency, value);
        } else {
            throw new ProductServiceException(PRICE_NOT_FOUND);
        }
    }

    /**
     * Update the currency and price information for a product in the aeropike store, inserting a new record if not
     * present.
     *
     * @param id       The product id
     * @param currency The currency code
     * @param price    The price of the product
     * @throws ProductServiceException In case DB is unreachable.
     */
    public void updateOrInsertPrice(String id, String currency, double price) throws ProductServiceException {
        Key asKey = new Key(namespace, priceSet, id);
        Bin currencyBin = new Bin(CURRENCY_BIN, currency);
        Bin priceBin = new Bin(VALUE_BIN, price);
        try {
            aerospikeClient.put(null, asKey, currencyBin, priceBin);
        } catch (AerospikeException ex) {
            throw new ProductServiceException(DB_ERROR);
        }
    }
}
