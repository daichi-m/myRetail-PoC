package com.myretail.product.core;

import static com.myretail.product.core.exception.ProductServiceException.FailureReason.API_ERROR;
import static com.myretail.product.core.exception.ProductServiceException.FailureReason.PRODUCT_NOT_FOUND;
import static com.myretail.product.core.exception.ProductServiceException.FailureReason.UNKNOWN;

import com.google.inject.Inject;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.myretail.product.ProductsServiceConfiguration;
import com.myretail.product.core.exception.ProductServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

/**
 * A Facade on top of the product details API to get the name for an ID.
 */
@Slf4j
public class DetailsApiFacade {

    private static String params = "fields=descriptions&id_type=TCIN";

    private final ProductsServiceConfiguration configuration;
    private final JsonPath jsonPath;

    @Inject
    public DetailsApiFacade(final ProductsServiceConfiguration configuration) {
        this.configuration = configuration;
        this.jsonPath = JsonPath.compile(configuration.getJsonPath());
    }

    /**
     * Given an object id, gets the name for it.
     *
     * @param id The id
     * @return The name corresponding to the id
     * @throws ProductServiceException In case the details are not found or the API is unreachable
     */
    public String getNameForId(String id) throws ProductServiceException {
        String urlPrefix = configuration.getProductDetailsAPI();
        String urlKey = configuration.getApiKey();
        String url = new StringBuilder(urlPrefix).append(id).append("?").append("key=" + urlKey)
                                                 .append("&").append(params).toString();
        log.debug("Details URL: {}", url);
        HttpGet request = new HttpGet(url);
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            CloseableHttpResponse httpResponse = client.execute(request);
            InputStream respStream = httpResponse.getEntity().getContent();
            return jsonPath.read(respStream);
        } catch (IOException ex) {
            throw new ProductServiceException(ex, API_ERROR);
        } catch (JsonPathException ex) {
            throw new ProductServiceException(ex, PRODUCT_NOT_FOUND);
        } catch (Exception ex) {
            throw new ProductServiceException(ex, UNKNOWN);
        }
    }
}
