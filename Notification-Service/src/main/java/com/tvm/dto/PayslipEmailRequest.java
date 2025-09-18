package com.tvm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PayslipEmailRequest {
    @Email @NotBlank
    private String to;
    @NotBlank
    private String employeeName;
    @NotBlank
    private String month;
    @NotBlank
    private String fileName; // pdf name to attach
    private byte[] pdf;      // base64 from other service or direct byte[] if calling internally
}
