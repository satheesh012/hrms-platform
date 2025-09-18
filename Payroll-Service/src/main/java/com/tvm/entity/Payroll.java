package com.tvm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;
    private String month; // e.g., 2025-07
    private Double baseSalary;
    private Double bonus;
    private Double deductions; // tax + unpaid leaves
    private Double netSalary;

    @Enumerated(EnumType.STRING)
    private Status status; // PROCESSED, PAID

    private LocalDate processedDate;

    public enum Status {
        PROCESSED, PAID
    }
}

