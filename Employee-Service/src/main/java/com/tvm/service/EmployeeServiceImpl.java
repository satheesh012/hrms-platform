package com.tvm.service;



import com.tvm.entity.Employee;
import com.tvm.exception.ResourceNotFoundException;
import com.tvm.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final EmployeeRepository repo;

    public EmployeeServiceImpl(EmployeeRepository repo) {
        this.repo = repo;
    }

    @Override
    public Employee create(Employee employee) {
        if (repo.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Employee with this email already exists");
        }
        return repo.save(employee);
    }

    public Employee updateEmployeeProfile(Long id, Employee employeeDetails) {
        Employee e = getById(id);
        e.setFirstName(employeeDetails.getFirstName());
        e.setLastName(employeeDetails.getLastName());
        e.setEmail(employeeDetails.getEmail());
        e.setPhone(employeeDetails.getPhone());
        e.setJobTitle(employeeDetails.getJobTitle());
        e.setDepartment(employeeDetails.getDepartment());
        e.setManagerId(employeeDetails.getManagerId());
        e.setSalary(employeeDetails.getSalary());
        return repo.save(e);
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        return repo.findByDepartment(department);
    }


    @Override
    public Employee getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
    }

    @Override
    public List<Employee> getAll() {
        return repo.findAll();
    }

    @Override
    public Employee update(Long id, Employee updated) {
        Employee e = getById(id);
        e.setFirstName(updated.getFirstName());
        e.setLastName(updated.getLastName());
        e.setEmail(updated.getEmail());
        e.setPhone(updated.getPhone());
        e.setDateOfJoining(updated.getDateOfJoining());
        e.setStatus(updated.getStatus());
        e.setJobTitle(updated.getJobTitle());
        e.setDepartment(updated.getDepartment());
        e.setSalary(updated.getSalary());
        e.setManagerId(updated.getManagerId());
        // contractFilePath stays unless you want to overwrite explicitly
        return repo.save(e);
    }

    @Override
    public Employee updateStatus(Long id, Employee.Status status) {
        Employee e = getById(id);
        e.setStatus(status);
        return repo.save(e);
    }

    @Override
    public void delete(Long id) {
        repo.delete(getById(id));
    }

    public Employee saveEmployeeWithFile(Employee employee, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            employee.setContractFilePath(filePath);
        }
        return repo.save(employee);
    }

    public Employee updateContractPath(Long id, String path) {
        Employee e = getById(id);
        e.setContractFilePath(path);
        return repo.save(e);
    }
}

