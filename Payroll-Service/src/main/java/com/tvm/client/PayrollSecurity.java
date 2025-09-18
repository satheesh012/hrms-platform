package com.tvm.client;

import com.tvm.dto.EmployeeDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class PayrollSecurity {

    private final EmployeeClient employeeClient;

    public PayrollSecurity(EmployeeClient employeeClient) {
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
