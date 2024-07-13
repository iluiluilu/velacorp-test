package com.linhnt.velaecommerce.config;

import org.springframework.stereotype.Component;

@Component
public class VeRequestContext {
    private static final ThreadLocal<String> requestId = new ThreadLocal<>();
    private static final ThreadLocal<String> deviceId = new ThreadLocal<>();

    public static String getRequestId() {
        return requestId.get();
    }

    public static void setRequestId(String value) {
        requestId.set(value);
    }

    public static String getDeviceId() {
        return deviceId.get();
    }

    public static void setDeviceId(String value) {
        deviceId.set(value);
    }

    public static void clear() {
        requestId.remove();
        deviceId.remove();
    }
}
