package com.tvm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "auth-service", configuration = FeignClientConfig.class)
public interface AuthClient {
    @GetMapping("/auth//usernames/by-role")
    public List<String> getUsernamesByRole(@RequestParam String role);
}

