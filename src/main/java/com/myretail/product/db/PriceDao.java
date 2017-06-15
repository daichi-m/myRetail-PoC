package com.myretail.product.db;

import com.aerospike.client.*;
import com.aerospike.client.policy.Policy;
import com.myretail.product.ProductsServiceConfiguration;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.luaj.vm2.ast.Str;

import javax.inject.Inject;

/**
 * DAO class to communicate to Aerospike for price related information.
 */
public class PriceDao {

    private final AerospikeClient aerospikeClient;
    private final String namespace;
    private final String priceSet;

    @Inject
    public PriceDao(final AerospikeClient aerospikeClient, final ProductsServiceConfiguration configuration) {
        this.aerospikeClient = aerospikeClient;
        this.namespace = configuration.getAerospikeNamespace();
        this.priceSet = configuration.getAerospikePriceSet();
    }

    private static final String CURRENCY_BIN = "curr";
    private static final String VALUE_BIN = "val";

    public Pair<String, Double> priceForId(String id) {
        Key asKey = new Key(namespace, priceSet, id);
        Record record = aerospikeClient.get(null, asKey);
        if (record != null) {
            String currency = record.getString(CURRENCY_BIN);
            Double value = record.getDouble(VALUE_BIN);
            return ImmutablePair.of(currency, value);
        } else {
            throw new AerospikeException(ResultCode.KEY_NOT_FOUND_ERROR);
        }
    }
}
