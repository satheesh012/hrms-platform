package com.tvm.service;

import com.tvm.client.EmployeeClient;
import com.tvm.dto.Employee;
import com.tvm.entity.Department;
import com.tvm.exception.ResourceNotFoundException;
import com.tvm.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {


    private final DepartmentRepository repo;
    private final EmployeeClient employeeClient;

    public DepartmentServiceImpl(DepartmentRepository repo, EmployeeClient employeeClient) {
        this.repo = repo;
        this.employeeClient = employeeClient;
    }

    @Override
    public Department createDepartment(Department department) {
        if (repo.existsByName(department.getName())) {
            throw new IllegalArgumentException("Department with this name already exists");
        }
        return repo.save(department);
    }

    @Override
    public Department updateDepartment(Long id, Department updated) {
        Department existing = getDepartmentById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setManagerId(updated.getManagerId());
        existing.setParentDepartmentId(updated.getParentDepartmentId());
        return repo.save(existing);
    }

    @Override
    public void deleteDepartment(Long id) {
        repo.delete(getDepartmentById(id));
    }



    @Override
    public Department assignManager(Long departmentId, Long managerId) {
        Department department = repo.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + departmentId));

        // Verify manager exists
        Employee manager = employeeClient.getEmployeeById(managerId);
        if (manager == null) {
            throw new IllegalArgumentException("Manager with ID " + managerId + " does not exist");
        }

        department.setManagerId(managerId);
        return repo.save(department);
    }


    @Override
    public List<Department> getAllDepartments() {
        return repo.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));
    }

    @Override
    public List<Department> getDepartmentHierarchy() {
        return repo.findAll(); // We'll later enhance this to return a proper tree
    }
}
