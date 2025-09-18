package com.tvm.service;



import com.tvm.entity.Payroll;

import java.util.List;

public interface PayrollService {
    Payroll processPayroll(Long employeeId, String month);
    Payroll getPayroll(Long employeeId, String month);
    List<Payroll> getPayrollsForEmployee(Long employeeId);
    List<Payroll> processAllForMonth(String month, List<Long> employeeIds);
}


