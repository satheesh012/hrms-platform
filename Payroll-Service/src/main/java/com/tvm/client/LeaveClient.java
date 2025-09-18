package com.tvm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "leave-service",configuration = com.tvm.client.FeignClientConfig.class)
public interface LeaveClient {
    @GetMapping("/api/leaves/approved")
    int getApprovedLeaves(@RequestParam Long employeeId, @RequestParam String month);
}
