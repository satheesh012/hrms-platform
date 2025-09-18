package com.tvm.service;

import com.tvm.entity.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AttendanceService {
    Attendance checkIn(Long employeeId);

    Attendance checkOut(Long employeeId);

    List<Attendance> getEmployeeAttendance(Long employeeId);

    List<Attendance> getAttendanceByDate(LocalDate date);

    Map<String, Object> getMonthlySummary(Long employeeId, String month);

    List<Attendance> getAttendanceReport(LocalDate startDate, LocalDate endDate);
}
