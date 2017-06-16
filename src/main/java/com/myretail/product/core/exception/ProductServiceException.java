package com.myretail.product.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An Exception type to wrap all the error scenarios in handling a product request.
 */
public class ProductServiceException extends Exception {

    @Getter
    private final FailureReason failureReason;

    public ProductServiceException(final FailureReason failureReason) {
        super(failureReason.message);
        this.failureReason = failureReason;
    }

    public ProductServiceException(final Throwable cause, final FailureReason failureReason) {
        super(failureReason.message, cause);
        this.failureReason = failureReason;
    }

    @AllArgsConstructor
    @Getter
    public static enum FailureReason {
        API_ERROR("Product API Unreachable"),
        PRODUCT_NOT_FOUND("Invalid Product"),
        DB_ERROR("Price DB Unreachable"),
        PRICE_NOT_FOUND("Price Information Not Available"),
        UNKNOWN("Unknown Error Occurred");

        private String message;
    }
}
