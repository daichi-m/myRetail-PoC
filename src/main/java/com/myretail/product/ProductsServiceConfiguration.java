package com.myretail.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.inject.Singleton;

@Getter
@ToString
@Singleton
public class ProductsServiceConfiguration extends Configuration {

    /* Aerospike connection string to get price info */
    @JsonProperty
    @NotBlank
    private String aerospikeConnectionString;

    @JsonProperty
    @NotEmpty
    private String aerospikeNamespace;

    @JsonProperty
    @NotEmpty
    private String aerospikePriceSet;

    /* The API path for the product details */
    @JsonProperty
    @NotEmpty
    private String productDetailsAPI;

    /* The key to call the API */
    @JsonProperty
    @NotEmpty
    private String productDetailsAPIKey;

    /* The JsonPath expression to get the details */
    @JsonProperty
    @NotEmpty
    private String jsonPath;

}
