package com.myretail.product.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * The Data object representing the final product details for the ID.
 */
@Builder
@Getter
@ToString
public class ProductDetails {

    @Builder
    @Getter
    @ToString
    public static class PriceInfo {
        @JsonProperty
        private double price;
        @JsonProperty("currency_code")
        private String currencyCode;
    }
    @JsonProperty("id")
    private String productId;
    @JsonProperty
    private String name;
    @JsonProperty("price_info")
    private PriceInfo priceInfo;
}
