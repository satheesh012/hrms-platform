package com.tvm.dto;

import com.tvm.entity.Leave;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data

public class ApplyLeaveRequest {
    @NotNull
    private Long employeeId;

    @NotNull
    private Leave.LeaveType leaveType;

    @NotNull @FutureOrPresent
    private LocalDate startDate;

    @NotNull @FutureOrPresent
    private LocalDate endDate;

    private String reason;
}
