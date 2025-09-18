package com.tvm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "leaves")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long employeeId;  // From employee-service

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType; // SICK, CASUAL, PAID, UNPAID, etc.

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;

    private String reason; // Optional leave reason

    private String managerComments; // Added by manager during approval/rejection

    private int days; // Duration of leave (calculated automatically)

    public enum LeaveType {
        SICK, CASUAL, PAID, UNPAID
    }

    public enum LeaveStatus {
        PENDING, APPROVED, REJECTED
    }

}

