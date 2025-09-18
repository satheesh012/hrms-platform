package com.tvm.client;

import com.tvm.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "notification-service", path = "/api/notifications")
public interface NotificationClient {

    @PostMapping("/send")
    void sendSimple(@RequestBody SimpleEmailRequest request);

    @PostMapping("/leave-status")
    void sendLeaveStatus(@RequestBody LeaveStatusEmailRequest request);

    @PostMapping("/onboarding")
    void sendOnboarding(@RequestBody OnboardingEmailRequest request);

    @PostMapping("/payslip")
    void sendPayslip(@RequestBody PayslipEmailRequest request);
}
