package com.tvm.service;

import com.tvm.entity.Department;

import java.util.List;

public interface DepartmentService {
    Department createDepartment(Department department);
    Department updateDepartment(Long id, Department department);
    void deleteDepartment(Long id);
    Department assignManager(Long departmentId, Long managerId);
    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    List<Department> getDepartmentHierarchy();
}
