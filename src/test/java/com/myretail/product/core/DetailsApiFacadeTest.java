package com.myretail.product.core;

import com.google.common.io.Resources;
import com.myretail.product.ProductsServiceConfiguration;
import com.myretail.product.core.exception.ProductServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.luaj.vm2.ast.Str;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Unit Test cases for DetailsApiFacade
 */
public class DetailsApiFacadeTest {

    @Mock
    ProductsServiceConfiguration configuration;
    @Mock
    HttpClient httpClient;

    private DetailsApiFacade detailsApiFacade;

    @BeforeTest
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doReturn("$.product_composite_response.items[0].online_description.value").when(configuration).getJsonPath();
        doReturn("https://api.target.com/products/v3/").when(configuration).getProductDetailsAPI();
        doReturn("43cJWpLjH8Z8oR18KdrZDBKAgLLQKJjz").when(configuration).getProductDetailsAPIKey();
        this.detailsApiFacade = new DetailsApiFacade(configuration, httpClient);
    }

    private String testUrl(String id) {
        return "https://api.target.com/products/v3/" + id + "?key=43cJWpLjH8Z8oR18KdrZDBKAgLLQKJjz&fields=descriptions&id_type=TCIN";
    }

    private CloseableHttpResponse getResponse(boolean success) throws IOException {
        InputStream entityStream;
        if (success) {
            entityStream = Resources.getResource("api-success.json").openStream();
        } else {
            entityStream = Resources.getResource("api-failure.json").openStream();
        }

        HttpEntity entity = mock(HttpEntity.class);
        doReturn(entityStream).when(entity).getContent();
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        doReturn(entity).when(response).getEntity();
        return response;
    }

    @Test
    public void detailsApiSuccessTest()throws Exception {
        reset(httpClient);
        String id = "123";
        ArgumentMatcher<HttpGet> uriMatcher = httpGet -> httpGet.getRequestLine().getUri().equals(testUrl(id));
        HttpResponse response = getResponse(true);
        doThrow(UnsupportedOperationException.class).when(httpClient).execute(any());
        doReturn(response).when(httpClient).execute(argThat(uriMatcher));

        String name = detailsApiFacade.getNameForId("123");

        Mockito.inOrder(httpClient, response);
        verify(httpClient, atLeastOnce()).execute(argThat(uriMatcher));
        verify(response, atLeastOnce()).getEntity();
        Assert.assertEquals(name, "The Big Lebowski (Blu-ray)");
    }

    @Test(expectedExceptions = ProductServiceException.class, expectedExceptionsMessageRegExp = "Invalid Product")
    public void detailsApiFailureTest()throws Exception {
        reset(httpClient);
        String id = "123";
        ArgumentMatcher<HttpGet> uriMatcher = httpGet -> httpGet.getRequestLine().getUri().equals(testUrl(id));
        HttpResponse response = getResponse(false);
        doThrow(UnsupportedOperationException.class).when(httpClient).execute(any());
        doReturn(response).when(httpClient).execute(argThat(uriMatcher));
        Exception thrownException = null;
        try {
            detailsApiFacade.getNameForId("123");
        } catch (Exception ex) {
            thrownException = ex;
        }

        Mockito.inOrder(httpClient, response);
        verify(httpClient, atLeastOnce()).execute(argThat(uriMatcher));
        verify(response, atLeastOnce()).getEntity();
        if (thrownException != null) {
            throw  thrownException;
        }
    }

    @Test(expectedExceptions = ProductServiceException.class, expectedExceptionsMessageRegExp = "Product API Unreachable")
    public void detailApiUnreachableTest() throws Exception {
        reset(httpClient);
        String id = "123";
        ArgumentMatcher<HttpGet> uriMatcher = httpGet -> httpGet.getRequestLine().getUri().equals(testUrl(id));
        HttpResponse response = getResponse(false);
        doThrow(SocketException.class).when(httpClient).execute(any());
        Exception thrownException = null;
        try {
            detailsApiFacade.getNameForId("123");
        } catch (Exception ex) {
            thrownException = ex;
        }

        Mockito.inOrder(httpClient, response);
        verify(httpClient, atLeastOnce()).execute(argThat(uriMatcher));
        verify(response, never()).getEntity();
        if (thrownException != null) {
            throw  thrownException;
        }
    }

    @Test(expectedExceptions = ProductServiceException.class, expectedExceptionsMessageRegExp = "Unknown Error Occurred")
    public void detailApiUnexpectedErrorTest() throws Exception {
        reset(httpClient);
        String id = "123";
        ArgumentMatcher<HttpGet> uriMatcher = httpGet -> httpGet.getRequestLine().getUri().equals(testUrl(id));
        HttpResponse response = getResponse(false);
        doThrow(Exception.class).when(httpClient).execute(any());
        Exception thrownException = null;
        try {
            detailsApiFacade.getNameForId("123");
        } catch (Exception ex) {
            thrownException = ex;
        }

        Mockito.inOrder(httpClient, response);
        verify(httpClient, atLeastOnce()).execute(argThat(uriMatcher));
        verify(response, never()).getEntity();
        if (thrownException != null) {
            throw  thrownException;
        }
    }

}
