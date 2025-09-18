package com.tvm.service;

import com.tvm.entity.Employee;

import java.util.List;

public interface EmployeeService {
    Employee create(Employee employee);

    Employee getById(Long id);

    List<Employee> getAll();

    Employee update(Long id, Employee employee);

    Employee updateStatus(Long id, Employee.Status status);

    void delete(Long id);

    Employee updateContractPath(Long id, String path);
}
