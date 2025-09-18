package com.tvm.service;


import com.tvm.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerTaskService {

    private final PayrollClient payrollClient;
    private final EmployeeClient employeeClient;
    private final AttendanceClient attendanceClient;
    private final LeaveClient leaveClient;
    private final MailService mailService;
    private final AuthClient authClient;

    @Value("${cron.payroll:*/30 * * * * ?}")
    private String payrollCron;

    @Value("${cron.attendance.daily:*/30 * * * * ?}")
    private String dailyAttendanceCron;

    @Value("${cron.attendance.weekly:*/30 * * * * ?}")
    private String weeklyAttendanceCron;

    @Value("${cron.reminders.leaves:*/30 * * * * ?}")
    private String leaveReminderCron;

    /**
     * ✅ 1) Monthly Payroll Generation (1st day of every month at 6 AM)
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void runMonthlyPayroll() {
        String month = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        List<Long> employees = employeeClient.getAllEmployeeIds();

        if (employees != null && !employees.isEmpty()) {
            payrollClient.processAll(month, employees);
            System.out.println("✅ Monthly payroll processed for: " + month);
        } else {
            System.out.println("⚠️ No employees found for payroll processing.");
        }
    }

    /**
     * ✅ 2) Daily Attendance Summary Email (every day 6:30 PM)
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void sendDailyAttendanceReport() {
        List<String> emails = employeeClient.getAllEmployeeEmails();
        attendanceClient.sendDailyAttendanceReport();

        for (String email : emails) {
            mailService.sendMail(email,
                    "Daily Attendance Summary",
                    "Here is your daily attendance summary report.");
        }
    }

    /**
     * ✅ 3) Weekly Attendance Summary Email (every Monday 10:00 AM)
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void sendWeeklyAttendanceReport() {
        List<String> emails = employeeClient.getAllEmployeeEmails();
        attendanceClient.sendWeeklyAttendanceReport();

        for (String email : emails) {
            mailService.sendMail(email,
                    "Weekly Attendance Report",
                    "Here is your weekly attendance summary.");
        }
    }

    /**
     * ✅ 4) Reminders for Pending Leave Approvals (every day 9:00 AM)
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void sendLeaveApprovalReminders() {
        // 1️⃣ Get admin usernames from Auth Service
        List<String> adminUsernames = authClient.getUsernamesByRole("ADMIN");

        // 2️⃣ Get emails of those admins from Employee Service
        List<String> emails = employeeClient.getEmailsByUsernames(adminUsernames);
        leaveClient.sendPendingLeaveReminders();

        for (String email : emails) {
            mailService.sendMail(email,
                    "Leave Approval Reminder",
                    "You have pending leave approvals. Please check and approve/reject them.");
        }
    }
}

