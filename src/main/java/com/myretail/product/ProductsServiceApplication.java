package com.myretail.product;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ProductsServiceApplication extends Application<ProductsServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ProductsServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "myReail Products Service API";
    }

    @Override
    public void initialize(Bootstrap<ProductsServiceConfiguration> bootstrap) {
        super.initialize(bootstrap);
    }

    @Override
    public void run(ProductsServiceConfiguration productsServiceConfiguration, Environment environment) throws Exception {

    }
}
