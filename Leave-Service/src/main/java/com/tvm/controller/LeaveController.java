package com.tvm.controller;


import com.tvm.dto.ApplyLeaveRequest;
import com.tvm.dto.ManagerActionRequest;
import com.tvm.entity.Leave;
import com.tvm.repository.LeaveRepository;
import com.tvm.service.LeaveService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService service;
    @Autowired
    private LeaveRepository leaveRepo;

    public LeaveController(LeaveService service) {
        this.service = service;
    }

    // ----------------- EMPLOYEE applies -----------------
    @PostMapping("/apply")
    @PreAuthorize("hasAnyRole('ADMIN','HR') or (hasRole('EMPLOYEE') and @leaveSecurity.isSelf(#req.employeeId, authentication))")
    public ResponseEntity<Leave> apply(@Valid @RequestBody ApplyLeaveRequest req) {
        if (req.getStartDate().isAfter(req.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        Leave leave = new Leave();
        leave.setEmployeeId(req.getEmployeeId());
        leave.setLeaveType(req.getLeaveType());
        leave.setStartDate(req.getStartDate());
        leave.setEndDate(req.getEndDate());
        leave.setReason(req.getReason());
        return ResponseEntity.ok(service.applyLeave(leave));
    }

    @GetMapping("/approved")
    public ResponseEntity<Integer> getApprovedLeaves(
            @RequestParam Long employeeId,
            @RequestParam String month) {

        // Calculate approved leaves for this employee for given month
        int approvedDays = leaveRepo.findAll().stream()
                .filter(l -> l.getEmployeeId().equals(employeeId)
                        && l.getStatus() == Leave.LeaveStatus.APPROVED
                        && l.getStartDate().getMonthValue() == Integer.parseInt(month.split("-")[1])
                        && l.getStartDate().getYear() == Integer.parseInt(month.split("-")[0]))
                .mapToInt(Leave::getDays)
                .sum();

        return ResponseEntity.ok(approvedDays);
    }


    // ----------------- MANAGER/HR/ADMIN approve/reject -----------------
    @PatchMapping("/{leaveId}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<Leave> approve(@PathVariable Long leaveId,
                                         @RequestBody ManagerActionRequest req) {
        return ResponseEntity.ok(service.approveLeave(leaveId, req.getManagerComments()));
    }

    @PatchMapping("/{leaveId}/reject")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<Leave> reject(@PathVariable Long leaveId,
                                        @RequestBody ManagerActionRequest req) {
        return ResponseEntity.ok(service.rejectLeave(leaveId, req.getManagerComments()));
    }

    // ----------------- Queries -----------------

    // Employee can only see his own leaves. Admin/HR can see anyone's.
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','HR') or (hasRole('EMPLOYEE') and @leaveSecurity.isSelf(#employeeId, authentication))")
    public ResponseEntity<List<Leave>> myLeaves(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getEmployeeLeaves(employeeId));
    }

    // Convenience for the logged-in employee (no need to pass id)
//    @GetMapping("/me")
//    //@PreAuthorize("hasRole('EMPLOYEE')")
//    public ResponseEntity<List<Leave>> myLeaves(Principal principal) {
//        Long employeeId = Long.valueOf(principal.getName());
//        return ResponseEntity.ok(service.getEmployeeLeaves(employeeId));
//    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<List<Leave>> pending() {
        return ResponseEntity.ok(service.getPendingLeaves());
    }

    @GetMapping("/balance/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','HR') or (hasRole('EMPLOYEE') and @leaveSecurity.isSelf(#employeeId, authentication))")
    public ResponseEntity<Integer> balance(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getLeaveBalance(employeeId));
    }

    // ✅ Dummy pending leave reminders
    @GetMapping("/reminders/pending")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public String sendPendingLeaveReminders() {
        // Just dummy log for now
        System.out.println("📧 Sending reminders for pending leave approvals...");
        return "Pending leave reminders sent (dummy)";
    }

}

