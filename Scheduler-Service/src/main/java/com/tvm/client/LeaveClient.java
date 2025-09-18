package com.tvm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "leave-service", path = "/api/leaves", configuration = FeignClientConfig.class)
public interface LeaveClient {
    @GetMapping("/reminders/pending")
    void sendPendingLeaveReminders();
}
