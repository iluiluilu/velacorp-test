package com.linhnt.velaecommerce.constant;

public class Constant {
    public static final String LOG_FORMAT = "{}|{}|{}|{}";

    public enum OrderStatus {
        NEW,
        SHIPPING,
        DONE,
        CANCEL,
        RETURN,
    }
}
