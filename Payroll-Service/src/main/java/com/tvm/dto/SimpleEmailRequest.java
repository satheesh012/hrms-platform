package com.tvm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleEmailRequest {
    private String to;         // Recipient email
    private String subject;    // Subject of email
    private String body;       // Email content
}
