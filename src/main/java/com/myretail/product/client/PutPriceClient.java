package com.myretail.product.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Locale;

/**
 * Test client to put price information
 */
@Slf4j
public class PutPriceClient {

    public static void main(String[] args) {

        String json = "{\"name\":\"The Big Lebowski (Blu-ray)\",\"id\":\"13860428\",\"price_info\":{\"price\":21," +
                "\"currency_code\":\"USD\"}}";
        HttpClient client = HttpClients.createDefault();
        HttpPut request = new HttpPut("http://localhost:8090/myRetail/v1/products/13860428");
        StringEntity jsonEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        request.setEntity(jsonEntity);
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
