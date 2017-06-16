package com.myretail.product.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Locale;

/**
 * Test client to get product details
 */
@Slf4j
public class GetProductDetailsClient {

    public static void main(String[] args) {

        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8090/myRetail/v1/products/13860428");
        try {
            CloseableHttpResponse response = (CloseableHttpResponse) client.execute(request);
            int respCode = response.getStatusLine().getStatusCode();
            String respReason = EnglishReasonPhraseCatalog.INSTANCE.getReason(respCode, Locale.ENGLISH);
            log.info("{} {}", respCode, respReason);
            String respString = EntityUtils.toString(response.getEntity());
            log.info("Response: {}", respString);
        } catch (IOException ex) {
            log.error("Error occurred", ex);
        }

    }

}
