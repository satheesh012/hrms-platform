package com.tvm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Department name is required")
    @Column(unique = true)
    private String name;

    private String description;

    private Long managerId; // Employee who is the manager (validated via FeignClient)

    private Long parentDepartmentId; // For hierarchy
}

