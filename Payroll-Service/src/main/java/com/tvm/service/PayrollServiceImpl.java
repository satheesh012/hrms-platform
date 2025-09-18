package com.tvm.service;

import com.tvm.client.AttendanceClient;
import com.tvm.client.EmployeeClient;
import com.tvm.client.LeaveClient;
import com.tvm.client.NotificationClient;
import com.tvm.dto.EmployeeDTO;
import com.tvm.dto.MonthlyAttendanceSummaryDTO;
import com.tvm.dto.PayslipEmailRequest;
import com.tvm.entity.Payroll;
import com.tvm.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepo;
    private final EmployeeClient employeeClient;
    private final AttendanceClient attendanceClient;
    private final LeaveClient leaveClient;
    private final PayslipService payslipService;
    private final NotificationClient notificationClient;

    @Value("${payroll.tax.percent:0.05}")
    private double taxPercent; // 5% default

    @Override
    public Payroll processPayroll(Long employeeId, String month) {
        payrollRepo.findByEmployeeIdAndMonth(employeeId, month)
                .ifPresent(p -> { throw new RuntimeException("Payroll already processed for this month"); });

        // 1) Employee (for salary + jobTitle etc.)
        EmployeeDTO emp = employeeClient.getEmployeeById(employeeId);
        if (emp == null || emp.getSalary() == null) {
            throw new RuntimeException("Employee or salary not found for id " + employeeId);
        }
        double baseSalary = emp.getSalary();

        // 2) Attendance summary
        MonthlyAttendanceSummaryDTO summary = attendanceClient.getMonthlySummary(employeeId, month);

        // 3) Approved leaves (assume all these are unpaid for now)
        int approvedLeaves = leaveClient.getApprovedLeaves(employeeId, month);

        // 4) Calculate salary parts
        double perDay = baseSalary / 30.0;
        double leaveDeduction = approvedLeaves * perDay;
        double tax = baseSalary * taxPercent;
        double bonus = 0.0; // plug your bonus rules here

        double totalDeductions = leaveDeduction + tax;
        double netSalary = baseSalary + bonus - totalDeductions;

        // 5) Persist
        Payroll payroll = Payroll.builder()
                .employeeId(employeeId)
                .month(month)
                .baseSalary(baseSalary)
                .bonus(bonus)
                .deductions(totalDeductions)
                .netSalary(netSalary)
                .status(Payroll.Status.PROCESSED)
                .processedDate(LocalDate.now())
                .build();

        Payroll saved = payrollRepo.save(payroll);
        byte[] pdf = payslipService.generatePayslip(employeeId, month);

        // Send payslip email
        PayslipEmailRequest emailReq = PayslipEmailRequest.builder()
                .to(emp.getEmail())
                .employeeName(emp.getFirstName() + " " + emp.getLastName())
                .month(month)
                .fileName("Payslip_" + month + ".pdf")
                .pdf(pdf)
                .build();

        notificationClient.sendPayslip(emailReq);

        return saved;
    }

    @Override
    public Payroll getPayroll(Long employeeId, String month) {
        return payrollRepo.findByEmployeeIdAndMonth(employeeId, month)
                .orElseThrow(() -> new RuntimeException("No payroll found for " + employeeId + " @ " + month));
    }

    @Override
    public List<Payroll> getPayrollsForEmployee(Long employeeId) {
        return payrollRepo.findByEmployeeId(employeeId);
    }

    @Override
    public List<Payroll> processAllForMonth(String month, List<Long> employeeIds) {
        List<Payroll> result = new ArrayList<>();
        for (Long empId : employeeIds) {
            try {
                result.add(processPayroll(empId, month));
            } catch (RuntimeException ex) {
                System.out.println("Payroll failed for empId={} month={} reason={}");
            }
        }
        return result;
    }
}
