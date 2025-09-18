package com.tvm.config;

import com.tvm.entity.Employee;
import com.tvm.service.EmployeeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("employeeSecurity")
public class EmployeeSecurity {

    private final EmployeeService employeeService;

    public EmployeeSecurity(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public boolean isSelf(Long id, Authentication authentication) {
        String username = authentication.getName(); // Extract username from JWT
        Employee emp = employeeService.getById(id);
        return emp != null && emp.getFirstName().equals(username); // Allow only if this is his account
    }
}
