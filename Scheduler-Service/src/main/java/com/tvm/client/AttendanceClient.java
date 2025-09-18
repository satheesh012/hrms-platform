package com.tvm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "attendance-service", path = "/api/attendance", configuration = FeignClientConfig.class)
public interface AttendanceClient {
    @GetMapping("/summary/send-daily")
    void sendDailyAttendanceReport();

    @GetMapping("/summary/send-weekly")
    void sendWeeklyAttendanceReport();
}
