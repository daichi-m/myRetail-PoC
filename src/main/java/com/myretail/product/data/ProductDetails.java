package com.myretail.product.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Data object representing the final product details for the ID.
 */
@Builder
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductDetails {

    @JsonIgnore
    private boolean completeInformation = true;
    @JsonProperty("id")
    private String productId;
    @JsonProperty
    private String name;
    @JsonProperty
    private Error error;
    @JsonProperty("price_info")
    private PriceInfo priceInfo;

    @Builder
    @Getter
    @ToString
    @NoArgsConstructor
    @EqualsAndHashCode
    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class PriceInfo {
        @JsonProperty
        private Double price;
        @JsonProperty("currency_code")
        private String currencyCode;
        @JsonProperty
        private Error error;
    }

    @Data
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Error {
        @JsonProperty
        private String message;
    }
}
