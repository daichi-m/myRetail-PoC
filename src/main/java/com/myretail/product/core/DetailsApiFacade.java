package com.myretail.product.core;

import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import com.google.inject.Inject;
import com.jayway.jsonpath.JsonPath;
import com.myretail.product.ProductsServiceConfiguration;
import com.sun.org.apache.xml.internal.utils.StringBufferPool;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * A Facade on top of the product details API to get the name for an ID.
 */
public class DetailsApiFacade {

    private static String params = "fields=descriptions&id_type=TCIN";

    /* String jsonPath = "$.product_composite_response.items[0].online_description.value"; */
    private final ProductsServiceConfiguration configuration;
    private final JsonPath jsonPath;

    @Inject
    public DetailsApiFacade(final ProductsServiceConfiguration configuration) {
        this.configuration = configuration;
        this.jsonPath = JsonPath.compile(configuration.getJsonPath());
    }

    public String getNameForId(String id) {
        String urlPrefix = configuration.getProductDetailsAPI();
        String urlKey = configuration.getApiKey();
        StringBuilder builder = new StringBuilder(urlPrefix).append(id).append("?").append("key=" + urlKey)
                .append("&").append(params);
        System.out.println("builder = " + builder);
        HttpGet request = new HttpGet(builder.toString());
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            CloseableHttpResponse httpResponse = client.execute(request);
            String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
            return jsonPath.read(jsonResponse);
        } catch (Exception ex) {
            // TODO: Handle failures
            ex.printStackTrace(System.err);
            throw new UnsupportedOperationException();
        }
    }
}
