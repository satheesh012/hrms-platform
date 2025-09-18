package com.tvm.config;

import com.tvm.client.EmployeeClient;
import com.tvm.dto.EmployeeDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class LeaveSecurity {

    private final EmployeeClient employeeClient;

    public LeaveSecurity(EmployeeClient employeeClient) {
        this.employeeClient = employeeClient;
    }

    public boolean isSelf(Long employeeId, Authentication authentication) {
        try {
            EmployeeDTO emp = employeeClient.getEmployeeById(employeeId);
            return emp != null && emp.getFirstName().equals(authentication.getName());
        } catch (Exception e) {
            return false;
        }
    }
}
