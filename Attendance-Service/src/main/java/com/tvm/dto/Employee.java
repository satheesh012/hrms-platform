package com.tvm.dto;

import lombok.Data;

@Data
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;
    private String jobTitle;
    private String status;
    private Double salary;
    private Long managerId;
}
