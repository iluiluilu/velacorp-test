package com.linhnt.velaecommerce.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_FOUND("NOT_FOUND", "Resource not found"),
    BAD_REQUEST("BAD_REQUEST", "Invalid request"),
    DELETE_PRODUCT_DENIED_BY_ORDER("BAD_REQUEST", "Denied delete because product is linked to at least one order"),
    UPDATE_ORDER_DENIED_BY_STATUS("BAD_REQUEST", "The current status of the order does not permit an update"),
    UPDATE_ORDER_CONTAIN_DUPLICATE_PRODUCT("BAD_REQUEST", "The order contains duplicate products"),
    UPDATE_ORDER_DATA_INCONSISTENT("BAD_REQUEST", "The order data is inconsistent"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "An unexpected error occurred");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
