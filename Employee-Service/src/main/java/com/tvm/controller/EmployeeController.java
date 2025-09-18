package com.tvm.controller;


import com.tvm.entity.Employee;
import com.tvm.repository.EmployeeRepository;
import com.tvm.service.EmployeeService;
import com.tvm.service.EmployeeServiceImpl;
import com.tvm.service.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeServiceImpl service;
    private final FileStorageService storage;

    @Autowired
    private EmployeeRepository employeeRepo;

    public EmployeeController(EmployeeServiceImpl service, FileStorageService storage) {
        this.service = service;
        this.storage = storage;
    }

    // -------- CRUD --------
    @PreAuthorize("hasAnyRole('ADMIN','HR','EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody Employee employee) {
        return ResponseEntity.ok(service.create(employee));
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR') or (hasRole('EMPLOYEE') and @employeeSecurity.isSelf(#id, authentication))")
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }




    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/by-department/{department}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(service.getEmployeesByDepartment(department));
    }


    @PreAuthorize("hasAnyRole('ADMIN','HR') or (hasRole('EMPLOYEE') and @employeeSecurity.isSelf(#id, authentication))")
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        return ResponseEntity.ok(service.update(id, employee));
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Employee> updateStatus(@PathVariable Long id, @RequestParam Employee.Status status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/all/ids")
    public List<Long> getAllEmployeeIds() {
        return employeeRepo.findAll().stream()
                .map(Employee::getId)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/all/emails")
    public List<String> getAllEmployeeEmails() {
        return employeeRepo.findAll().stream()
                .map(Employee::getEmail)
                .toList();
    }
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PostMapping("/emails/by-usernames")
    public List<String> getEmailsByUsernames(@RequestBody List<String> usernames) {
        return employeeRepo.findByFirstNameIn(usernames)
                .stream()
                .map(Employee::getEmail)
                .toList();
    }



    // -------- Contract upload / download --------
    @PreAuthorize("hasAnyRole('ADMIN','HR') or (hasRole('EMPLOYEE') and @employeeSecurity.isSelf(#id, authentication))")
    @PostMapping("/{id}/contract")
    public ResponseEntity<String> uploadContract(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String path = storage.save(file);
            service.updateContractPath(id, path);
            return ResponseEntity.ok("Uploaded: " + path);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR') or (hasRole('EMPLOYEE') and @employeeSecurity.isSelf(#id, authentication))")
    @GetMapping("/{id}/contract")
    public ResponseEntity<byte[]> downloadContract(@PathVariable Long id) {
        try {
            Employee emp = service.getById(id);
            if (emp.getContractFilePath() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Path.of(emp.getContractFilePath());
            byte[] bytes = Files.readAllBytes(filePath);

            String originalFileName = filePath.getFileName().toString();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"")
                    .contentType(MediaType.APPLICATION_PDF) // Force PDF content type
                    .body(bytes);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}



