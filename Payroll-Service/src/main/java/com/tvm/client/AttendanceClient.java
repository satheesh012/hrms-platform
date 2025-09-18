package com.tvm.client;

import com.tvm.dto.MonthlyAttendanceSummaryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "attendance-service", configuration = com.tvm.client.FeignClientConfig.class)
public interface AttendanceClient {
    @GetMapping("/api/attendance/employee/{employeeId}/summary")
    MonthlyAttendanceSummaryDTO getMonthlySummary(
            @PathVariable Long employeeId,
            @RequestParam String month
    );
}
