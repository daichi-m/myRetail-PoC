package com.myretail.product.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by daichi on 16/6/17.
 */
@ToString
@AllArgsConstructor
@Getter
public class PriceUpdateStatus {

    public static  final PriceUpdateStatus PRICE_UPDATED = new PriceUpdateStatus(200, "Price Updated Successfully");
    public static  final PriceUpdateStatus UNAVAILABLE = new PriceUpdateStatus(503, "DB Unavailable");
    public static  final PriceUpdateStatus BAD_REQUEST = new PriceUpdateStatus(400, "Id or Price Information Not Provided");

    @JsonProperty
    int code;
    @JsonProperty
    String message;
}
