package com.tvm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeaveStatusEmailRequest {
    @Email @NotBlank
    private String to;
    @NotBlank
    private String employeeName;
    @NotBlank
    private String status;           // APPROVED / REJECTED
    private String managerComments;  // optional
    @NotBlank
    private String leaveRange;       // e.g. 2025-08-01 to 2025-08-03
}
