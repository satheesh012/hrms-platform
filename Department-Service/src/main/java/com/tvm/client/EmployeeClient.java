package com.tvm.client;

import com.tvm.dto.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "employee-service", path = "/api/employees")
public interface EmployeeClient {

    @GetMapping("/{id}")
    Employee getEmployeeById(@PathVariable("id") Long id);

    @GetMapping("/by-department/{department}")
    List<Employee> getEmployeesByDepartment(@PathVariable("department") String department);
}
