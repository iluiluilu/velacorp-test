package com.linhnt.velaecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
public class RequestInterceptor implements HandlerInterceptor {
    private static final String REQUEST_ID_HEADER = "requestId";
    private static final String DEVICE_ID_HEADER = "deviceId";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        String deviceId = request.getHeader(DEVICE_ID_HEADER);

        if (!StringUtils.hasText(requestId)) {
            requestId = UUID.randomUUID().toString();
        }

        if (!StringUtils.hasText(deviceId)) {
            deviceId = UUID.randomUUID().toString();
        }

        VeRequestContext.setRequestId(requestId);
        VeRequestContext.setDeviceId(deviceId);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        VeRequestContext.clear();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
    }
}
