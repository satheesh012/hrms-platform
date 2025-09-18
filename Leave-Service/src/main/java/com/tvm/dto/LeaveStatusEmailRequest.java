package com.tvm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveStatusEmailRequest {
    private String to;             // Recipient email
    private String employeeName;   // Employee first name or full name
    private String leaveRange;     // e.g., "2025-07-01 to 2025-07-05"
    private String status;         // APPROVED or REJECTED
    private String managerComments;// Optional manager comments
}
