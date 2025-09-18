package com.tvm.controller;

import com.tvm.entity.Payroll;
import com.tvm.service.PayrollService;
import com.tvm.service.PayslipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;
    private final PayslipService payslipService;

    // HR / ADMIN: process one employee
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PostMapping("/process/{employeeId}")
    public ResponseEntity<Payroll> processPayroll(
            @PathVariable Long employeeId,
            @RequestParam String month
    ) {
        return ResponseEntity.ok(payrollService.processPayroll(employeeId, month));
    }

    // HR / ADMIN: process many employees for the month
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PostMapping("/process-all")
    public ResponseEntity<List<Payroll>> processAll(
            @RequestParam String month,
            @RequestBody List<Long> employeeIds
    ) {
        return ResponseEntity.ok(payrollService.processAllForMonth(month, employeeIds));
    }

    // HR / ADMIN or EMPLOYEE (self) can get payroll for a month

    @GetMapping("/employee/{employeeId}/by-month")
    @PreAuthorize("hasAnyRole('ADMIN','HR') or (hasRole('EMPLOYEE') and @payrollSecurity.isSelf(#employeeId, authentication))")
    public ResponseEntity<Payroll> getPayrollForMonth(
            @PathVariable Long employeeId,
            @RequestParam String month
    ) {
        return ResponseEntity.ok(payrollService.getPayroll(employeeId, month));
    }

    // HR / ADMIN or EMPLOYEE (self) get all payrolls

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','HR') or (hasRole('EMPLOYEE') and @payrollSecurity.isSelf(#employeeId, authentication))")
    public ResponseEntity<List<Payroll>> getPayrollHistory(@PathVariable Long employeeId) {
        return ResponseEntity.ok(payrollService.getPayrollsForEmployee(employeeId));
    }



    @GetMapping("/{employeeId}/payslip")
    @PreAuthorize("hasAnyRole('ADMIN','HR') or (hasRole('EMPLOYEE') and @payrollSecurity.isSelf(#employeeId, authentication))")
    public ResponseEntity<byte[]> downloadPayslip(@PathVariable Long employeeId,
                                                  @RequestParam String month) {
        byte[] pdf = payslipService.generatePayslip(employeeId, month);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payslip-" + employeeId + "-" + month + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}

