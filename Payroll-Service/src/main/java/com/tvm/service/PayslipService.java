package com.tvm.service;

import com.tvm.client.EmployeeClient;
import com.tvm.dto.EmployeeDTO;
import com.tvm.entity.Payroll;
import com.tvm.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PayslipService {

    private final PayrollRepository payrollRepo;
    private final EmployeeClient employeeClient;

    public byte[] generatePayslip(Long employeeId, String month) {
        Payroll payroll = payrollRepo.findByEmployeeIdAndMonth(employeeId, month)
                .orElseThrow(() -> new RuntimeException("Payroll not found for this month"));

        EmployeeDTO employee = employeeClient.getEmployeeById(employeeId);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(document, out);
            document.open();

            document.add(new com.lowagie.text.Paragraph("Payslip for " + month));
            document.add(new com.lowagie.text.Paragraph(" "));
            document.add(new com.lowagie.text.Paragraph("Employee Name: " + employee.getFirstName() + " " + employee.getLastName()));
            document.add(new com.lowagie.text.Paragraph("Email: " + employee.getEmail()));
            document.add(new com.lowagie.text.Paragraph("Job Title: " + employee.getJobTitle()));
            document.add(new com.lowagie.text.Paragraph(" "));
            document.add(new com.lowagie.text.Paragraph("Base Salary: " + payroll.getBaseSalary()));
            document.add(new com.lowagie.text.Paragraph("Deductions: " + payroll.getDeductions()));
            document.add(new com.lowagie.text.Paragraph("Bonus: " + payroll.getBonus()));
            document.add(new com.lowagie.text.Paragraph("Net Salary: " + payroll.getNetSalary()));
            document.add(new com.lowagie.text.Paragraph("Processed On: " + payroll.getProcessedDate()));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating payslip PDF: " + e.getMessage(), e);
        }
    }
}

