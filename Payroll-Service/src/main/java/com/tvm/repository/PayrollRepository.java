package com.tvm.repository;

import com.tvm.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    Optional<Payroll> findByEmployeeIdAndMonth(Long employeeId, String month);

    List<Payroll> findByEmployeeId(Long employeeId);
}

