package com.tvm.service;

import com.tvm.entity.Leave;

import java.util.List;

public interface LeaveService {
    Leave applyLeave(Leave leave);
    Leave approveLeave(Long leaveId, String managerComments);
    Leave rejectLeave(Long leaveId, String managerComments);
    List<Leave> getEmployeeLeaves(Long employeeId);
    List<Leave> getPendingLeaves();
    int getLeaveBalance(Long employeeId);
}

