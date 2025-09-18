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
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYXRoZWVzaCIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzUzODc4MDI3LCJleHAiOjE3NTM4ODE2Mjd9.Qsoy726Ym3lBDv4x1mRYaRUSylx6BCEnDXcY8BdwyLM";
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("Authorization", "Bearer " + SERVICE_TOKEN);
        };
    }
}


