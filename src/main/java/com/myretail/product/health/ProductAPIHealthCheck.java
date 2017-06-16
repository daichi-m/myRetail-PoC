package com.myretail.product.health;

import com.hubspot.dropwizard.guice.InjectableHealthCheck;
import com.myretail.product.core.ProductHandler;
import com.myretail.product.data.ProductDetails;

import javax.inject.Inject;

/**
 * A HealthCheck that fetches the details for a fixed product ID (which will only fail if either the details API or
 * Aerospike becomes unreachable.
 */
public class ProductAPIHealthCheck extends InjectableHealthCheck {

    private static final String TEST_ID = "13860428";
    private final ProductHandler handler;

    @Inject
    public ProductAPIHealthCheck(final ProductHandler productHandler) {
        this.handler = productHandler;
    }

    @Override
    protected Result check() throws Exception {
        ProductDetails details = handler.getDetails(TEST_ID);
        if (details.getError() == null && details.getPriceInfo().getError() == null) {
            return Result.healthy();
        } else if (details.getError() != null) {
            return Result.unhealthy("Details API Unreachable");
        } else {
            return Result.unhealthy("Aerospike Unreachable");
        }
    }

    @Override
    public String getName() {
        return "ProductAPIHealthCheck";
    }
}
