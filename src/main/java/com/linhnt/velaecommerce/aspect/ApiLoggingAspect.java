package com.linhnt.velaecommerce.aspect;

import com.google.gson.Gson;
import com.linhnt.velaecommerce.config.VeRequestContext;
import com.linhnt.velaecommerce.constant.Constant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.linhnt.velaecommerce.VelaEcommerceApplication.appName;

@Aspect
@Component
@Slf4j
public class ApiLoggingAspect {

    @Before("execution(* com.linhnt.velaecommerce.controller..*(..)) && args(..)")
    public void logUserDto(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();


        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        String requestUrl = "";
        Map<String, List<String>> headersMap = new HashMap<>();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            requestUrl = request.getMethod() + " "  +request.getRequestURL().toString();

            headersMap = Collections.list(request.getHeaderNames())
                    .stream()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            h -> Collections.list(request.getHeaders(h))
                    ));
        }
        String requestInfo = String.format("Request info: %s | Header %s | Method params %s", requestUrl, new Gson().toJson(headersMap), new Gson().toJson(args));
        log.info(Constant.LOG_FORMAT, VeRequestContext.getDeviceId(), VeRequestContext.getRequestId(), appName, requestInfo);
    }
}
