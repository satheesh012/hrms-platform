package com.tvm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OnboardingEmailRequest {
    @Email @NotBlank
    private String to;
    @NotBlank
    private String employeeName;
}
