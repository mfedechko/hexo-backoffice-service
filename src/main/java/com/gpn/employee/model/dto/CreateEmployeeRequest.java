package com.gpn.employee.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Schema(description = "Details required to add a new employee")
public record CreateEmployeeRequest(
        @Schema(description = "First name", example = "Jane")
        @NotBlank(message = "First name is required")
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
        Long parentId) {
}
