package com.tvm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingEmailRequest {
    private String to;            // Recipient email
    private String employeeName;  // Name of new employee
}
