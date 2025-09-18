package com.tvm.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (!(attrs instanceof ServletRequestAttributes servletAttrs)) {
            return; // no HTTP context (e.g., scheduler thread)
        }

        HttpServletRequest request = servletAttrs.getRequest();
        String auth = request.getHeader("Authorization");
        if (auth != null && !auth.isBlank()) {
            template.header("Authorization", auth);
        }
    }
}

