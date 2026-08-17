package com.gpn.employee.controller;

import com.gpn.employee.model.EmployeeStatus;
import com.gpn.employee.model.dto.CreateEmployeeRequest;
import com.gpn.employee.model.dto.EmployeeDto;
import com.gpn.employee.model.dto.EmployeeFilterRequest;
import com.gpn.employee.model.dto.UpdateEmployeeRequest;
import com.gpn.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
@Tag(name = "Employees", description = "Backoffice employee management")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @Operation(
            summary = "List all employees",
            description = "Returns employees matching the given filters, newest first. All filters are optional."
    )
    @ApiResponse(responseCode = "200", description = "Employees returned")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(
            @Parameter(description = "First name contains (case-insensitive)") @RequestParam(required = false) String firstName,
            @Parameter(description = "Last name contains (case-insensitive)") @RequestParam(required = false) String lastName,
            @Parameter(description = "Phone contains") @RequestParam(required = false) String phone,
            @Parameter(description = "Email contains (case-insensitive)") @RequestParam(required = false) String email,
            @Parameter(description = "Department contains (case-insensitive)") @RequestParam(required = false) String department,
            @Parameter(description = "Employee status") @RequestParam(required = false) EmployeeStatus status,
            @Parameter(description = "Created at, from (inclusive)") @RequestParam(required = false) LocalDateTime createdFrom,
            @Parameter(description = "Created at, to (inclusive)") @RequestParam(required = false) LocalDateTime createdTo) {
        return ResponseEntity.ok(employeeService.getAllEmployees(
                new EmployeeFilterRequest(firstName, lastName, phone, email, department, status, createdFrom, createdTo)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an employee by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
    })
    public ResponseEntity<EmployeeDto> getEmployeeById(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @PostMapping
    @Operation(summary = "Add a new employee")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee created"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
    })
    public ResponseEntity<EmployeeDto> addEmployee(@Valid @RequestBody final CreateEmployeeRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeService.addEmployee(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modify an existing employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
    })
    public ResponseEntity<EmployeeDto> modifyEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id,
            @Valid @RequestBody final UpdateEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.modifyEmployee(id, request));
    }

    @PostMapping("/{id}/suspend")
    @Operation(summary = "Suspend an employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee suspended"),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
    })
    public ResponseEntity<EmployeeDto> suspendEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        return ResponseEntity.ok(employeeService.suspendEmployee(id));
    }

    @PostMapping("/{id}/reactivate")
    @Operation(summary = "Reactivate a suspended employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee reactivated"),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
    })
    public ResponseEntity<EmployeeDto> reactivateEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        return ResponseEntity.ok(employeeService.reactivateEmployee(id));
    }
}
