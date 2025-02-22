package com.example.readinggenerationservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    public static String getBearerTokenHeader() {
        if((ServletRequestAttributes) RequestContextHolder.getRequestAttributes() != null){
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest().getHeader("Authorization");
        }
        else {
            return null;
        }
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
//        log.info(getBearerTokenHeader().toString());
        requestTemplate.header(AUTHORIZATION_HEADER, getBearerTokenHeader());
    }
}