package com.gpn.employee.model.dto;

import com.gpn.employee.model.EmployeeStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "An employee managed in the backoffice")
public record EmployeeDto(
        @Schema(description = "Employee ID", example = "1")
        Long id,

        @Schema(description = "First name", example = "Jane")
        String firstName,

        @Schema(description = "Middle name", nullable = true)
        String middleName,

        @Schema(description = "Last name", nullable = true)
        String lastName,

        @Schema(description = "Contact phone number", nullable = true)
        String phone,

        @Schema(description = "Contact email", nullable = true)
        String email,

        @Schema(description = "Date of birth", nullable = true)
        LocalDateTime birthday,

        @Schema(description = "Department", nullable = true)
        String department,

        @Schema(description = "ID of the parent/manager employee", nullable = true)
        Long parentId,

        @Schema(description = "Employee status", example = "ACTIVE")
        EmployeeStatus status,

        @Schema(description = "Timestamp the employee was created")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp the employee was last updated", nullable = true)
        LocalDateTime updatedAt) {
}
