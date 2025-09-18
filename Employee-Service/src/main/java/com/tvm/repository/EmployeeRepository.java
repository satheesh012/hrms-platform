package com.tvm.repository;

import com.tvm.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);
    List<Employee> findByDepartment(String department);
    List<Employee> findByManagerId(Long managerId);

    List<Employee> findByFirstNameIn(List<String> usernames);
}
