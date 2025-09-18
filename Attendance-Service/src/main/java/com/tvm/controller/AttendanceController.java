package com.tvm.controller;

import com.tvm.entity.Attendance;
import com.tvm.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/check-in/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<Attendance> checkIn(@PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.checkIn(employeeId));
    }


    @PostMapping("/check-out/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<Attendance> checkOut(@PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.checkOut(employeeId));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<List<Attendance>> getEmployeeAttendance(@PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.getEmployeeAttendance(employeeId));
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(@PathVariable String date) {
        return ResponseEntity.ok(attendanceService.getAttendanceByDate(LocalDate.parse(date)));
    }

    @GetMapping("/employee/{employeeId}/summary")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<Map<String, Object>> getMonthlySummary(
            @PathVariable Long employeeId,
            @RequestParam String month
    ) {
        return ResponseEntity.ok(attendanceService.getMonthlySummary(employeeId, month));
    }

    @GetMapping("/report")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<List<Attendance>> getAttendanceReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(attendanceService.getAttendanceReport(startDate, endDate));
    }


    // ✅ Dummy daily summary email endpoint
    @GetMapping("/summary/send-daily")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public String sendDailyAttendanceSummary() {
        // Just dummy log for now
        System.out.println("📧 Sending DAILY attendance summary email...");
        return "Daily attendance summary email sent (dummy)";
    }

    // ✅ Dummy weekly summary email endpoint
    @GetMapping("/summary/send-weekly")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public String sendWeeklyAttendanceSummary() {
        // Just dummy log for now
        System.out.println("📧 Sending WEEKLY attendance summary email...");
        return "Weekly attendance summary email sent (dummy)";
    }



}
