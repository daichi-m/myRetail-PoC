package com.myretail.product.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * The Data object representing the final product details for the ID.
 */
@Builder
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductDetails {

    @Builder
    @Getter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class PriceInfo {
        @JsonProperty
        private Double price;
        @JsonProperty("currency_code")
        private String currencyCode;
        @JsonProperty
        private Error error;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class Error {
        @JsonProperty
        private String message;
    }

    @JsonProperty("id")
    private String productId;
    @JsonProperty
    private String name;
    @JsonProperty
    private Error error;
    @JsonProperty("price_info")
    private PriceInfo priceInfo;
}
