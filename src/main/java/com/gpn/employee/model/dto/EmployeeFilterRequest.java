package com.gpn.employee.model.dto;

import com.gpn.employee.model.EmployeeStatus;

import java.time.LocalDateTime;

public record EmployeeFilterRequest(
        String firstName,
        String lastName,
        String phone,
        String email,
        String department,
        EmployeeStatus status,
        LocalDateTime createdFrom,
        LocalDateTime createdTo) {
}
