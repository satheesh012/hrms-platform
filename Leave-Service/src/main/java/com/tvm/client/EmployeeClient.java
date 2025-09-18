package com.tvm.client;

import com.tvm.dto.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service", path = "/api/employees",
        configuration = com.tvm.client.FeignAuthInterceptor.class)
public interface EmployeeClient {

    @GetMapping("/{id}")
    EmployeeDTO getEmployeeById(@PathVariable("id") Long id);
}
