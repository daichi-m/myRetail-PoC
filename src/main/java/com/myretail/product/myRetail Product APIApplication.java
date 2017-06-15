package com.myretail.product;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class myRetail Product APIApplication extends Application<myRetail Product APIConfiguration> {

    public static void main(final String[] args) throws Exception {
        new myRetail Product APIApplication().run(args);
    }

    @Override
    public String getName() {
        return "myRetail Product API";
    }

    @Override
    public void initialize(final Bootstrap<myRetail Product APIConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final myRetail Product APIConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
