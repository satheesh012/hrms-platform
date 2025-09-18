package com.tvm.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "leave_balance")
@Data
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;
    private int totalLeaves = 12;   // e.g., 12 per year
    private int usedLeaves = 0;
}

