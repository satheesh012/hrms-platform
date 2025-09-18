package com.tvm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAttendanceSummaryDTO {
    private Long employeeId;
    private String month;                 // e.g. "2025-07"
    private int totalDaysPresent;
    private long totalHoursWorkedMinutes;
    private String totalHoursWorkedHHmm;  // pretty formatted time like "120:30"
}
