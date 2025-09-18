package com.tvm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayslipEmailRequest {
    private String to;             // Recipient email
    private String employeeName;   // Employee first name
    private String month;          // Month of payroll
    private String fileName;       // Filename for PDF (e.g., Payslip-July.pdf)
    private byte[] pdf;            // Payslip PDF file as byte array
}
