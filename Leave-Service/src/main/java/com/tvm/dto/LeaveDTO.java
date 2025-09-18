package com.tvm.dto;

import com.tvm.entity.Leave;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveDTO {
    private Long id;
    private Long employeeId;
    private Leave.LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Leave.LeaveStatus status;
    private String reason;
    private String managerComments;
    private int days;
}

