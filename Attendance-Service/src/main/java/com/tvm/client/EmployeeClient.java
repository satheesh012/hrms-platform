package com.tvm.client;

import com.tvm.dto.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service", path = "/api/employees", configuration = FeignClientConfig.class)
public interface EmployeeClient {

    @GetMapping("/{id}")
    Employee getEmployeeById(@PathVariable("id") Long id);
}

