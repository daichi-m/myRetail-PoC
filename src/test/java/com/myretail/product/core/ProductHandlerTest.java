package com.myretail.product.core;

import com.myretail.product.core.exception.ProductServiceException;
import com.myretail.product.data.PriceUpdateStatus;
import com.myretail.product.data.ProductDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static com.myretail.product.core.exception.ProductServiceException.FailureReason.*;

/**
 * Testcase for ProductHandler
 */
@Slf4j
public class ProductHandlerTest {

    @Mock DetailsApiFacade detailsApiFacade;
    @Mock PriceInfoHandler priceInfoHandler;
    private ProductHandler productHandler;

    private final static String NAME = "The Big Lebowski (Blu-ray)";
    private final static String ID = "13860428";
    private final static String CURRENCY = "USD";
    private final static Double PRICE = 25.0;

    @BeforeTest
    public void setup() {
        MockitoAnnotations.initMocks(this);
        productHandler = new ProductHandler(detailsApiFacade, priceInfoHandler);
    }

    private ProductDetails getTestProductDetails(boolean namePresent, boolean pricePresent) {
        ProductDetails.PriceInfo priceInfo = ProductDetails.PriceInfo.builder().currencyCode(CURRENCY).price(PRICE).build();
        ProductDetails.Error nameError = new ProductDetails.Error("Invalid Product");
        ProductDetails.Error priceError = new ProductDetails.Error("Price Information Not Available");
        ProductDetails.PriceInfo invalidPriceInfo = ProductDetails.PriceInfo.builder().error(priceError).build();

        ProductDetails.ProductDetailsBuilder detailsBuilder = ProductDetails.builder().productId(ID).completeInformation(true);
        if (namePresent) {
            detailsBuilder.name(NAME);
        } else {
            detailsBuilder.error(nameError).completeInformation(false);
        }

        if (pricePresent) {
            detailsBuilder.priceInfo(priceInfo);
        } else {
            detailsBuilder.priceInfo(invalidPriceInfo).completeInformation(false);
        }
        return detailsBuilder.build();
    }

    @DataProvider(name = "getDetailsDataProvider")
    private Object[][] provideGetDetailsTestData() {
        return new Object[][] {
                {true, true},
                {true, false},
                {false, true},
                {false, false}
        };
    }

    @Test(dataProvider = "getDetailsDataProvider")
    public void getDetailsTest(boolean namePresent, boolean pricePresent) throws Exception {

        reset(detailsApiFacade, priceInfoHandler);
        if (namePresent) {
            doReturn(NAME).when(detailsApiFacade).getNameForId(ID);
        } else {
            doThrow(new ProductServiceException(PRODUCT_NOT_FOUND)).when(detailsApiFacade).getNameForId(ID);
        }
        if (pricePresent) {
            doReturn(ImmutablePair.of(CURRENCY, PRICE)).when(priceInfoHandler).priceForId(ID);
        } else {
            doThrow(new ProductServiceException(PRICE_NOT_FOUND)).when(priceInfoHandler).priceForId(ID);
        }

        ProductDetails expectedDetails = getTestProductDetails(namePresent, pricePresent);
        ProductDetails retrievedDetails = productHandler.getDetails(ID);

        Assert.assertEquals(retrievedDetails, expectedDetails);
    }


    @Test
    public void updatePriceDetailsTest() throws Exception {
        ProductDetails testProductDetails = getTestProductDetails(true, true);
        reset(priceInfoHandler);
        doNothing().when(priceInfoHandler).updateOrInsertPrice(ID, CURRENCY, PRICE);

        PriceUpdateStatus status = productHandler.updatePriceDetails(ID, testProductDetails);
        Assert.assertEquals(status.getCode(), PriceUpdateStatus.PRICE_UPDATED.getCode());
    }


    @Test
    public void updatePriceDetailsFailureTest() throws Exception {
        ProductDetails testProductDetails = getTestProductDetails(true, true);
        reset(priceInfoHandler);
        doThrow(new ProductServiceException(DB_ERROR)).when(priceInfoHandler).updateOrInsertPrice(ID, CURRENCY, PRICE);

        PriceUpdateStatus status = productHandler.updatePriceDetails(ID, testProductDetails);
        Assert.assertEquals(status.getCode(), PriceUpdateStatus.UNAVAILABLE.getCode());
    }


    @Test
    public void updatePriceDetailsInvalidTest1() throws Exception {
        reset(priceInfoHandler);
        doNothing().when(priceInfoHandler).updateOrInsertPrice(ID, CURRENCY, PRICE);

        PriceUpdateStatus status = productHandler.updatePriceDetails(ID, null);
        Assert.assertEquals(status.getCode(), PriceUpdateStatus.BAD_REQUEST.getCode());
    }

    @Test
    public void updatePriceDetailsInvalidTest2() throws Exception {
        reset(priceInfoHandler);
        ProductDetails details = ProductDetails.builder().productId(ID).name(NAME).build();
        doNothing().when(priceInfoHandler).updateOrInsertPrice(ID, CURRENCY, PRICE);

        PriceUpdateStatus status = productHandler.updatePriceDetails(ID, details);
        Assert.assertEquals(status.getCode(), PriceUpdateStatus.BAD_REQUEST.getCode());
    }
}
