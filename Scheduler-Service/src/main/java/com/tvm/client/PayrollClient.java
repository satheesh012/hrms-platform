package com.tvm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "payroll-service", path = "/api/payroll", configuration = FeignClientConfig.class)
public interface PayrollClient {

    @PostMapping("/process-all")
    ResponseEntity<?> processAll(
            @RequestParam String month,
            @RequestBody List<Long> employeeIds
    );
}
