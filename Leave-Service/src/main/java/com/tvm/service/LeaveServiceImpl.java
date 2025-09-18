package com.tvm.service;

import com.tvm.client.EmployeeClient;
import com.tvm.client.NotificationClient;
import com.tvm.dto.EmployeeDTO;
import com.tvm.dto.LeaveStatusEmailRequest;
import com.tvm.entity.Leave;
import com.tvm.entity.LeaveBalance;
import com.tvm.repository.LeaveBalanceRepository;
import com.tvm.repository.LeaveRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepo;
    private final EmployeeClient employeeClient;
    private final LeaveBalanceRepository leaveBalance;

    @Autowired
    private NotificationClient notificationClient;

    public LeaveServiceImpl(LeaveRepository leaveRepo, EmployeeClient employeeClient, LeaveBalanceRepository leaveBalance) {
        this.leaveRepo = leaveRepo;
        this.employeeClient = employeeClient;
        this.leaveBalance = leaveBalance;
    }

    @Override
    public Leave applyLeave(Leave leave) {
        // Validate employee exists
        try {
            employeeClient.getEmployeeById(leave.getEmployeeId());
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("Employee not found with ID: " + leave.getEmployeeId());
        }


        // Check leave balance
        int balance = getLeaveBalance(leave.getEmployeeId());
        if (leave.getDays() > balance) {
            throw new IllegalStateException("Not enough leave balance available");
        }

        leave.setStatus(Leave.LeaveStatus.PENDING);
        return leaveRepo.save(leave);
    }


    @Override
    public Leave approveLeave(Long leaveId, String managerComments) {
        Leave leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        int days = (int) ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;

        LeaveBalance balance = leaveBalance.findByEmployeeId(leave.getEmployeeId())
                .orElseGet(() -> {
                    LeaveBalance newBalance = new LeaveBalance();
                    newBalance.setEmployeeId(leave.getEmployeeId());
                    newBalance.setTotalLeaves(20);  // default
                    newBalance.setUsedLeaves(0);
                    return leaveBalance.save(newBalance);
                });

        // ✅ Check leave limit before approval
        if (balance.getUsedLeaves() + days > balance.getTotalLeaves()) {
            // Send rejection email to employee
            try {
                EmployeeDTO emp = employeeClient.getEmployeeById(leave.getEmployeeId());
                LeaveStatusEmailRequest mailReq = new LeaveStatusEmailRequest(
                        emp.getEmail(),
                        emp.getFirstName(),
                        leave.getStartDate() + " to " + leave.getEndDate(),
                        "REJECTED",
                        "Leave request rejected. You have exceeded your yearly leave limit of "
                                + balance.getTotalLeaves() + " days."
                );
                notificationClient.sendLeaveStatus(mailReq);
            } catch (Exception e) {
                System.out.println("⚠️ Notification service failed: " + e.getMessage());
            }

            // Reject the leave request
            leave.setStatus(Leave.LeaveStatus.REJECTED);
            leave.setManagerComments("Leave limit exceeded (" + balance.getTotalLeaves() + " days)");
            return leaveRepo.save(leave);
        }

        // ✅ Approve leave if limit is not exceeded
        leave.setStatus(Leave.LeaveStatus.APPROVED);
        leave.setManagerComments(managerComments);
        leave.setDays(days);
        // Update leave balance

        balance.setUsedLeaves(balance.getUsedLeaves() + days);
        leaveBalance.save(balance);
        leaveRepo.save(leave);

        EmployeeDTO emp = employeeClient.getEmployeeById(leave.getEmployeeId());
        LeaveStatusEmailRequest mailReq = new LeaveStatusEmailRequest(
                emp.getEmail(),
                emp.getFirstName(),
                leave.getStartDate() + " to " + leave.getEndDate(),
                "APPROVED",
                managerComments
        );
        notificationClient.sendLeaveStatus(mailReq);

        return leave;
    }


    @Override
    public Leave rejectLeave(Long leaveId, String managerComments) {
        Leave leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + leaveId));
        leave.setStatus(Leave.LeaveStatus.REJECTED);
        leave.setManagerComments(managerComments);
        leaveRepo.save(leave);

        EmployeeDTO emp = employeeClient.getEmployeeById(leave.getEmployeeId());
        LeaveStatusEmailRequest mailReq = new LeaveStatusEmailRequest(
                emp.getEmail(),
                emp.getFirstName(),
                leave.getStartDate() + " to " + leave.getEndDate(),
                "REJECTED",
                managerComments
        );
        notificationClient.sendLeaveStatus(mailReq);

        return leave;
    }

    @Override
    public List<Leave> getEmployeeLeaves(Long employeeId) {
        return leaveRepo.findByEmployeeId(employeeId);
    }

    @Override
    public List<Leave> getPendingLeaves() {
        return leaveRepo.findByStatus(Leave.LeaveStatus.PENDING);
    }

    @Override
    public int getLeaveBalance(Long employeeId) {
        return leaveBalance.findByEmployeeId(employeeId)
                .map(b -> b.getTotalLeaves() - b.getUsedLeaves())
                .orElse(20); // default 20 leaves if not found
    }

}

