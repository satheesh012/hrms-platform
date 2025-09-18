package com.tvm.client;

import com.tvm.dto.LeaveStatusEmailRequest;
import com.tvm.dto.PayslipEmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service",
        configuration = com.tvm.client.FeignAuthInterceptor.class)
public interface NotificationClient {

    @PostMapping("/api/notifications/leave-status")
    void sendLeaveStatus(@RequestBody LeaveStatusEmailRequest request);

    @PostMapping("/api/notifications/payslip")
    void Payslip(@RequestBody PayslipEmailRequest request);
}
