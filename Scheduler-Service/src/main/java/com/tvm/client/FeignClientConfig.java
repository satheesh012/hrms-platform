package com.tvm.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    private static final String SERVICE_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTYXRoZWVzaCIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzc3NjQzNDc0LCJleHAiOjE4MDkxNzk0NzR9.t_86a_zpdyuUjCDhOFZzdNxtbDtxcxaDZavfxzzoacw";
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("Authorization", "Bearer " + SERVICE_TOKEN);
        };
    }
}


