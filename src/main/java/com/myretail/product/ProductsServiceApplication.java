package com.myretail.product;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.myretail.product.resources.ProductsResource;
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
    public void run(ProductsServiceConfiguration productsServiceConfiguration, Environment environment) throws
            Exception {
    }

    @Override
    public void initialize(Bootstrap<ProductsServiceConfiguration> bootstrap) {
        GuiceBundle<ProductsServiceConfiguration> guiceBundle = GuiceBundle.<ProductsServiceConfiguration>newBuilder()
                .addModule(new ProductsServiceModule())
                .setConfigClass(ProductsServiceConfiguration.class)
                .enableAutoConfig("com.myretail.product")
                .build();
        bootstrap.addBundle(guiceBundle);
    }
}
